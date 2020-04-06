package ru.javaops.masterjava.service.mail.model;

import lombok.*;

import javax.mail.internet.InternetAddress;
import java.util.List;

@Data
@NoArgsConstructor
@ToString(callSuper = true)
public class Mail  {

    public Mail(List<InternetAddress> to, String errorResult) {

        this.to = to;
        this.errorResult = errorResult;
    }

     private List<InternetAddress> to;
    @NonNull private String errorResult;
}