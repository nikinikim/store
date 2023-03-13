package me.project.socksstoreapp.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.project.socksstoreapp.exceptions.NotEnoughSocksException;
import me.project.socksstoreapp.model.Color;
import me.project.socksstoreapp.model.Composition;
import me.project.socksstoreapp.model.Size;
import me.project.socksstoreapp.model.Sock;
import me.project.socksstoreapp.services.SockService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/api/socks")
@Tag(name = "Носки", description = "CRUD - операции для работы с складом носков")
public class SockController {
    private final SockService sockService;

    public SockController(SockService sockService) {
        this.sockService = sockService;
    }

    @PostMapping
    @Operation(summary = "Добавление товара")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Товар добавлен")})
    public ResponseEntity<Sock> addSocks(@Valid @RequestBody Sock sock) {
        sockService.addSocks(sock);
        return ResponseEntity.ok(sock);
    }

    @GetMapping("/all")
    @Operation(summary = "Получение всего списка товаров")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Возвращает весь список товаров")})
    public ResponseEntity<Collection<Sock>>getAllSocks() {
        return ResponseEntity.ok(sockService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Поиск товара по id")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Товар найден")})
    @Parameters(value = {@Parameter(name = "id", example = "1")})
    public ResponseEntity<Sock> getSocksById(@PathVariable long id) {
        return ResponseEntity.ok(sockService.getSocks(id));
    }

    @GetMapping("/color/{color}")
    @Operation(summary = "Получение товара по цвету")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Возвращает товар по цвету")})
    @Parameters(value = {@Parameter(name = "Цвет", example = "GRAY")})
    public ResponseEntity<Sock> getSocksByColor(@PathVariable Color color) {
        return ResponseEntity.ok(sockService.getSocksByColor(color));
    }

    @GetMapping("/size/{size}")
    @Operation(summary = "Получение товара по размеру")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Возвращает товар по цвету")})
    @Parameters(value = {@Parameter(name = "Размер", example = "М")})
    public ResponseEntity<Sock> getSocksBySize(@PathVariable Size size) {
        return ResponseEntity.ok(sockService.getSocksBySize(size));
    }

    @GetMapping("/composition/{composition}")
    @Operation(summary = "Получение товара по составу")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Возвращает товар по составу")})
    @Parameters(value = {@Parameter(name = "Процентное содержание хлопка", example = "10")})
    public ResponseEntity<Sock> getSocksByComposition(@PathVariable Composition composition) {
        return ResponseEntity.ok(sockService.getSocksByComposition(composition));
    }

    @GetMapping("/")
    @Operation(summary = "Получение количества товара по параметрам")
    public ResponseEntity<String> getSocksQuantityByParameters(
            @RequestParam(required = false) Color color,
            @RequestParam(required = false) Size size,
            @RequestParam(name = "cottonPart", required = false) Integer cottonPercentageMin,
            @RequestParam(name = "cottonPart", required = false) Integer cottonPercentageMax) {
        if ((cottonPercentageMin != null && cottonPercentageMax != null) && (cottonPercentageMin > cottonPercentageMax)) {
            return ResponseEntity.badRequest().body("Минимальное количество хлопка должно быть меньше или равно максимальному количеству");
        }
        int totalQuantity = sockService.getSocksQuantityByParameters(color, size, cottonPercentageMin, cottonPercentageMax);
        return ResponseEntity.ok().body(String.valueOf(totalQuantity));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Отпуск товара со склада")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Товар отпущен",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Sock.class))})})
    @Parameters(value = {@Parameter(name = "id", example = "1")})
    public ResponseEntity<String> sellSocks(@RequestBody Sock sock) {
        try {
            Sock editedSock = sockService.editSocks(sock.getId(), sock);
            return ResponseEntity.ok("Товар успешно отпущен со склада");
        } catch (NotFoundException | NotEnoughSocksException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка на сервере");
        }
    }

    @DeleteMapping("/api/socks/{id}")
    @Operation(summary = "Удаление товара")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Товар удален")})
    @Parameters(value = {@Parameter(name = "id", example = "1")})
    public ResponseEntity<Sock> deleteSocks(@PathVariable("id") Long id) {
        return ResponseEntity.ok(sockService.deleteSocks(id));
    }
}
