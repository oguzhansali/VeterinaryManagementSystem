package dev.patika.VetManagementSystem.core.result;

import lombok.Getter;

@Getter
public class ResultData <T> extends Result {
    private T data;

    // ResultData sınıfının yapıcı metodu
    // Bu metod, genel sonuç durumu (success/failure),
    // mesaj, kod ve veri içeren yanıtı oluşturur.
    public ResultData(boolean status, String message, String code, T data){
        super(status,message,code);
        this.data=data;
    }

}
