package com.example.asm.service;

import com.example.asm.model.CartDetail;

import java.util.List;

public interface CartsService {
    List<CartDetail> getItems();
    void add(int id);
    void remove(int id);
    void clear();
    void update(int id, int qty);
    int getTotal();
    int getAmount();
}
