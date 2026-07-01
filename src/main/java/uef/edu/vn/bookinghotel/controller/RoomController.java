package uef.edu.vn.bookinghotel.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Locale;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uef.edu.vn.bookinghotel.authorization.RoleRequired;
import uef.edu.vn.bookinghotel.model.Room;
import uef.edu.vn.bookinghotel.model.RoomType;
import uef.edu.vn.bookinghotel.service.RoomService;
import uef.edu.vn.bookinghotel.service.RoomTypeService;

@Controller
public class RoomController {

    private final RoomService roomService;
    private final RoomTypeService roomTypeService;

    public RoomController(RoomService roomService, RoomTypeService roomTypeService) {
        this.roomService = roomService;
        this.roomTypeService = roomTypeService;
    }

    @GetMapping("/rooms")
    public String rooms(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "roomTypeId", required = false) Integer roomTypeId,
            @RequestParam(value = "status", required = false) String status,
            Model model) {
        model.addAttribute("rooms", roomService.rooms(keyword, roomTypeId, status));
        model.addAttribute("roomTypes", roomTypeService.roomTypes());
        return "public/rooms/list";
    }

    @GetMapping("/rooms/detail")
    public String detail(@RequestParam("id") int id, Model model) {
        model.addAttribute("room", roomService.room(id));
        return "public/rooms/detail";
    }

    @GetMapping("/rooms/edit")
    @RoleRequired({"STAFF", "ADMIN"})
    public String edit(@RequestParam("id") int id, Model model, HttpSession session) {
        String guard = Access.requireRole(session, "STAFF", "ADMIN");
        if (guard != null) {
            return guard;
        }

        Room room = roomService.room(id);
        if (room == null) {
            return "redirect:/rooms";
        }

        model.addAttribute("room", room);
        model.addAttribute("roomTypes", roomTypeService.roomTypes());
        return "public/rooms/edit";
    }

    @PostMapping("/rooms")
    @RoleRequired({"STAFF", "ADMIN"})
    public String saveRoom(
            @ModelAttribute Room room,
            @RequestParam(value = "originalId", required = false) Integer originalId,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            HttpServletRequest request,
            HttpSession session,
            RedirectAttributes redirectAttributes) throws IOException {
        String guard = Access.requireRole(session, "STAFF", "ADMIN");
        if (guard != null) {
            return guard;
        }

        String uploadedImage = saveRoomImage(imageFile, request);
        if (uploadedImage != null) {
            room.setImageUrl(uploadedImage);
        }
        try {
            if (originalId == null) {
                roomService.saveRoom(room);
            } else {
                roomService.saveRoom(originalId, room);
            }
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("roomError", ex.getMessage());
            if (originalId != null) {
                return "redirect:/rooms/edit?id=" + originalId;
            }
            return "redirect:/rooms";
        }
        return "redirect:/rooms";
    }

    @GetMapping("/rooms/history")
    @RoleRequired({"MANAGER", "ADMIN"})
    public String roomHistory(Model model, HttpSession session) {
        String guard = Access.requireRole(session, "MANAGER", "ADMIN");
        if (guard != null) {
            return guard;
        }

        model.addAttribute("history", roomService.roomChangeHistory());
        return "admin/rooms/history";
    }

    @PostMapping("/rooms/delete")
    @RoleRequired("ADMIN")
    public String deleteRoom(@RequestParam("id") int id, HttpSession session) {
        String guard = Access.requireRole(session, "ADMIN");
        if (guard != null) {
            return guard;
        }

        roomService.deleteRoom(id);
        return "redirect:/rooms";
    }

    @PostMapping("/rooms/status")
    @RoleRequired({"STAFF", "ADMIN"})
    public String status(
            @RequestParam("id") int id,
            @RequestParam("status") String status,
            HttpSession session) {
        String guard = Access.requireRole(session, "STAFF", "ADMIN");
        if (guard != null) {
            return guard;
        }

        roomService.updateRoomStatus(id, status);
        return "redirect:/rooms";
    }

    @GetMapping("/room-types")
    @RoleRequired({"RECEPTIONIST", "MANAGER", "ADMIN"})
    public String types(Model model, HttpSession session) {
        String guard = Access.requireRole(session, "RECEPTIONIST", "MANAGER", "ADMIN");
        if (guard != null) {
            return guard;
        }

        model.addAttribute("roomTypes", roomTypeService.roomTypes());
        return "admin/room-types/list";
    }

    @PostMapping("/room-types")
    @RoleRequired("ADMIN")
    public String saveType(@ModelAttribute RoomType type, HttpSession session) {
        String guard = Access.requireRole(session, "ADMIN");
        if (guard != null) {
            return guard;
        }

        roomTypeService.saveRoomType(type);
        return "redirect:/room-types";
    }

    @PostMapping("/room-types/delete")
    @RoleRequired("ADMIN")
    public String deleteType(@RequestParam("id") int id, HttpSession session) {
        String guard = Access.requireRole(session, "ADMIN");
        if (guard != null) {
            return guard;
        }

        roomTypeService.deleteRoomType(id);
        return "redirect:/room-types";
    }

    private String saveRoomImage(MultipartFile imageFile, HttpServletRequest request) throws IOException {
        if (imageFile == null || imageFile.isEmpty()) {
            return null;
        }

        String originalName = imageFile.getOriginalFilename();
        String extension = extensionOf(originalName);
        String fileName = "room-" + System.currentTimeMillis() + extension;
        String realPath = request.getServletContext().getRealPath("/assets/img/rooms");
        if (realPath == null) {
            throw new IOException("Cannot resolve room image upload folder.");
        }

        byte[] imageBytes = imageFile.getBytes();
        Path uploadDir = Paths.get(realPath);
        Files.createDirectories(uploadDir);
        Files.write(uploadDir.resolve(fileName), imageBytes, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        Path sourceAssetDir = sourceRoomAssetDir(request);
        if (sourceAssetDir != null && !sourceAssetDir.equals(uploadDir)) {
            Files.createDirectories(sourceAssetDir);
            Files.write(sourceAssetDir.resolve(fileName), imageBytes, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        }

        return "/assets/img/rooms/" + fileName;
    }

    private Path sourceRoomAssetDir(HttpServletRequest request) {
        String appRoot = request.getServletContext().getRealPath("/");
        if (appRoot == null) {
            return null;
        }

        Path current = Paths.get(appRoot).toAbsolutePath();
        while (current != null) {
            if (current.getFileName() != null && "target".equalsIgnoreCase(current.getFileName().toString())) {
                Path projectRoot = current.getParent();
                Path sourceWebapp = projectRoot.resolve("src").resolve("main").resolve("webapp");
                if (Files.exists(sourceWebapp)) {
                    return sourceWebapp.resolve("assets").resolve("img").resolve("rooms");
                }
            }
            current = current.getParent();
        }
        return null;
    }

    private String extensionOf(String originalName) {
        if (originalName == null || !originalName.contains(".")) {
            return ".jpg";
        }

        String extension = originalName.substring(originalName.lastIndexOf('.')).toLowerCase(Locale.ROOT);
        if (extension.matches("\\.(jpg|jpeg|png|gif|webp)")) {
            return extension;
        }
        return ".jpg";
    }
}
