package net.onest.time;

import net.onest.time.api.RandomWordApi;

import org.junit.Test;

public class RandomWordApiTest {

    @Test
    public void getRandomWord() {
        String randomWord = RandomWordApi.getRandomWord();
        System.out.println(randomWord);
    }
}
