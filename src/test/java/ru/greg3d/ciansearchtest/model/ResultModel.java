package ru.greg3d.ciansearchtest.model;

import ru.greg3d.annotations.Alias;
import ru.greg3d.helpers.model.BaseModel;

public class ResultModel extends BaseModel{

    @Alias("Параметры объекта")
    public String title;

    @Alias("Стоимость")
    public String price;

    @Alias("Локация")
    public String location;

    @Alias("Описание")
    public String description;

    @Alias("Владелец заявки")
    public String owner;
}
