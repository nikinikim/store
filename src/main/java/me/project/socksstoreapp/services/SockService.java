package me.project.socksstoreapp.services;

import me.project.socksstoreapp.model.Color;
import me.project.socksstoreapp.model.Composition;
import me.project.socksstoreapp.model.Size;
import me.project.socksstoreapp.model.Sock;

import java.util.Collection;


public interface SockService {
    Sock addSocks(Sock sock);

    Sock getSocks(Long id);

    Sock getSocksByColor(Color color);

    int getSocksQuantityByParameters(Color color, Size size, int cottonPercentageMin, int cottonPercentageMax);

    Sock getSocksBySize(Size size);

    Sock getSocksByComposition(Composition composition);

    //Sock editSocks(Long id, Sock sock);

    /*@Override
     public Sock editSocks(Long id, Sock sock) {
         return sockMap.entrySet().stream().filter(entry -> entry.getKey().equals(id))
                 .findFirst().map(entry ->{sockMap.put(id, sock);
                 return sock;
                 }).orElseThrow(() -> new NotFoundException("Товар по указанному id не найден"));
     }*/
    Sock editSocks(Long id, Sock sock);

    Sock deleteSocks(Long id);

    Collection<Sock> getAll();
}
