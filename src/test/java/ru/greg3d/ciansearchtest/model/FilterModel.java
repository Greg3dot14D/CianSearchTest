package ru.greg3d.ciansearchtest.model;

import ru.greg3d.annotations.Alias;
import ru.greg3d.helpers.model.BaseModel;

public class FilterModel extends BaseModel{

    @Alias("Действие")
    public String action;

    @Alias("Тип недвижимости")
    public String type;

//    @Alias("Категория")
//    public String category;

    @Alias("Число комнат")
    public String roomsCount;

    @Alias("Минимальная цена")
    public Double priceMin;

    @Alias("Максимальная цена")
    public Double priceMax;

    @Alias("Регион")
    public String region;
}
