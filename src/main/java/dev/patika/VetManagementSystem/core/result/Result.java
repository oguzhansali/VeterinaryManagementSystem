package dev.patika.VetManagementSystem.core.result;

import lombok.Getter;

@Getter
public class Result {
    private boolean status;
    private String message;
    private String code;


    // Yapıcı metod: Result sınıfını başlatmak için kullanılır
    // Bu metod, işlem durumunu, mesajını ve kodunu ayarlar
    public Result(boolean status, String message, String code) {
        this.status = status;
        this.message = message;
        this.code = code;
    }
}
