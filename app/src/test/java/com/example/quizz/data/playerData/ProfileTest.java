package com.example.quizz.data.playerData;


import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class ProfileTest {


    static Profile profile;
    static Player player;


    @Before
    public void init(){
        profile = new Profile();
        player = new Player();
        player.setPlayerName("Test");
        profile.addPlayer(player);
    }


    @Test
    public void addPlayer(){
        assertEquals(1, profile.getPlayerListSize());

        profile.addPlayer(new Player());

        assertEquals(2, profile.getPlayerListSize());
        assertEquals("Test", profile.getPlayerList().get(0).getPlayerName());
    }

    @Test
    public void getPlayerList(){
        profile.addPlayer(player);
        List<Player> list = profile.getPlayerList();

        assertEquals(2, list.size());
    }

    @Test
    public void nameValidation(){
        assertEquals(-1, profile.nameIsValid("Test"));
        assertEquals(-2, profile.nameIsValid("A"));
        assertEquals(-2, profile.nameIsValid("ABCDEFGHIJK"));
        assertEquals(1, profile.nameIsValid("Peter"));
    }

    @Test
    public void getPlayerWithName(){
        assertEquals(player, profile.getPlayerWithName("Test"));
    }

    @Test
    public void removePlayer(){
        assertEquals(1, profile.getPlayerListSize());

        profile.removePlayerFromList("Test");

        assertEquals(0, profile.getPlayerListSize());
    }

}