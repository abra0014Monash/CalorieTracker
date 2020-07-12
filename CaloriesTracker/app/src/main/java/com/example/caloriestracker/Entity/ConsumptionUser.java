package com.example.caloriestracker.Entity;

import java.math.BigDecimal;
import java.util.Date;

public class ConsumptionUser {

    protected ConsumptionUserPK consumptionUserPK;
    private BigDecimal quantity;
    private FoodInternal food;
    private UserTable userTable;

    public ConsumptionUser() {
    }

    public ConsumptionUser(ConsumptionUserPK consumptionUserPK) {
        this.consumptionUserPK = consumptionUserPK;
    }

    public ConsumptionUser(int userid, Date date, int foodid) {
        this.consumptionUserPK = new ConsumptionUserPK(userid, date, foodid);
    }

    public ConsumptionUserPK getConsumptionUserPK() {
        return consumptionUserPK;
    }

    public void setConsumptionUserPK(ConsumptionUserPK consumptionUserPK) {
        this.consumptionUserPK = consumptionUserPK;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public FoodInternal getFood() {
        return food;
    }

    public void setFood(FoodInternal food) {
        this.food = food;
    }

    public UserTable getUserTable() {
        return userTable;
    }

    public void setUserTable(UserTable userTable) {
        this.userTable = userTable;
    }
}
