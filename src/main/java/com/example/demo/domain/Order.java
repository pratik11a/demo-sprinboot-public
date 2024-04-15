package com.example.demo.domain;

import java.util.List;

public record Order(List<OrderItem> items) { }
