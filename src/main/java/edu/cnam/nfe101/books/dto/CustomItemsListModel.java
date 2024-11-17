package edu.cnam.nfe101.books.dto;

import java.util.List;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.RepresentationModel;

public class CustomItemsListModel<T> extends RepresentationModel<CustomItemsListModel<T>>{

    private List<EntityModel<T>> items;

    public CustomItemsListModel(List<EntityModel<T>> items) {
        this.items = items;
    }

    public List<EntityModel<T>> getItems() {
        return items;
    }

    

    
}
