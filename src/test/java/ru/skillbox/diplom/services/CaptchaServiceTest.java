package ru.skillbox.diplom.services;

import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.skillbox.diplom.entities.CaptchaCode;
import ru.skillbox.diplom.repositories.CaptchaRepositori;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CaptchaServiceTest {

    @Autowired
    CaptchaService captchaService;
    @Autowired
    CaptchaRepositori captchaRepositori;

    @Test
    public void genAndSaveCaptcha() {
        long deltaTime = 3_600_000;

        CaptchaCode captchaCode = new CaptchaCode();
        captchaCode.setCode("Test_code");
        captchaCode.setSecretCode("Test_secret_code");
        captchaCode.setTime(System.currentTimeMillis());
        captchaRepositori.save(captchaCode);

        CaptchaCode captchaCodeOld = new CaptchaCode();
        captchaCodeOld.setCode("Test_code_old");
        captchaCodeOld.setSecretCode("Test_secret_code_oid");
        captchaCodeOld.setTime(System.currentTimeMillis() - 7_200_100);
        captchaRepositori.save(captchaCodeOld);

        CaptchaCode captchaCodeOld_1 = new CaptchaCode();
        captchaCodeOld_1.setCode("Test_code_old");
        captchaCodeOld_1.setSecretCode("Test_secret_code_oid");
        captchaCodeOld_1.setTime(System.currentTimeMillis() - 3_600_100);
        captchaRepositori.save(captchaCodeOld_1);

        captchaRepositori.deleteOldCaptca(System.currentTimeMillis() - deltaTime);

        captchaRepositori.count();
        Assert.assertEquals(1, captchaRepositori.count());

    }
}