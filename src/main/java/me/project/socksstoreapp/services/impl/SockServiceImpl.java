package me.project.socksstoreapp.services.impl;

import me.project.socksstoreapp.exceptions.NotEnoughSocksException;
import me.project.socksstoreapp.model.Color;
import me.project.socksstoreapp.model.Composition;
import me.project.socksstoreapp.model.Size;
import me.project.socksstoreapp.model.Sock;
import me.project.socksstoreapp.services.SockService;
import org.springframework.stereotype.Repository;
import org.webjars.NotFoundException;

import java.util.*;


@Repository
public class SockServiceImpl implements SockService {

    private Map<Long, Sock> sockMap = new HashMap<>();

    private static long id = 1;

    public Map<Long, Sock> getSockMap() {
        return sockMap;
    }

    @Override
    public Sock addSocks(Sock sock) {
        sockMap.put(id++, sock);
        return sock;
    }

    @Override
    public Sock getSocks(Long id) {
        return sockMap.values().stream()
                .filter(sock -> sock.getId() == id)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Товар по указанному id не найден"));
    }

    @Override
    public Sock getSocksByColor(Color color) {
        return sockMap.values().stream()
                .filter(sock -> sock.getColor() == color)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Товар по указанному цвету не найден"));
    }

    @Override
    public int getSocksQuantityByParameters(Color color, Size size, int cottonPercentageMin, int cottonPercentageMax) {
        return sockMap.values().stream()
                .filter(sock -> (color == null || sock.getColor() == color) &&
                        (size == null || sock.getSize() == size) &&
                        (sock.getComposition().getCottonPercentage() >= cottonPercentageMin) &&
                        (sock.getComposition().getCottonPercentage() <= cottonPercentageMax))
                .mapToInt(Sock::getQuantity).sum();
    }

    @Override
    public Sock getSocksBySize(Size size) {
        return sockMap.values().stream()
                .filter(sock -> sock.getSize() == size)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Товар по указанному размеру не найден"));
    }

    @Override
    public Sock getSocksByComposition(Composition composition) {
        return sockMap.values().stream()
                .filter(sock -> sock.getComposition() == composition)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Товар по указанному составу не найден"));
    }

    @Override
    public Sock editSocks(Long id, Sock sock) {
        Sock storedSock = sockMap.values().stream()
                .filter(s -> s.getColor().equals(sock.getColor()) &&
                        s.getSize().equals(sock.getSize()) && s.getComposition().equals(sock.getComposition()))
                .findFirst().orElseThrow(() -> new NotFoundException("Носки с указанными параметрами отсутствуют на складе"));
        int storedQuantity = storedSock.getQuantity();
        int requestQuantity = sock.getQuantity();
        if (requestQuantity > storedQuantity) {
            throw new NotEnoughSocksException("На складе нет запрашиваемого количества товара с указанными параметрами");
        }
        int editedQuantity = storedQuantity - requestQuantity;
        storedSock.setQuantity(editedQuantity);
        return storedSock;
    }

    @Override
    public Sock deleteSocks(Long id) {
        return sockMap.entrySet().stream()
                .filter(entry -> entry.getKey().equals(id))
                .findFirst()
                .map(entry -> {
                    Sock deleteSocks = entry.getValue();
                    sockMap.remove(id);
                    return deleteSocks;
                })
                .orElseThrow(() -> new NotFoundException("Товар по указанному id не найден"));
    }

    @Override
    public Collection<Sock> getAll() {
        return sockMap.values();
    }
}
