package com.example.quizz.questionManager;


/**
 *
 *  <p>9 = General Knowledge</p>
 *  <p>10 = Entertainment: Books</p>
 *  <p>11 = Entertainment: Music</p>
 *  <p>12 = Entertainment: Film</p>
 *  <p>13 = Entertainment: Musicals & Theatres</p>
 *  <p>14 = Entertainment: Television</p>
 *  <p>15 = Entertainment: Video Games</p>
 *  <p>16 = Entertainment: Board Games</p>
 *  <p>17 = Science & Nature</p>
 *  <p>18 = Science: Computers</p>
 *  <p>19 = Science: Mathematics</p>
 *  <p>20 = Mythology</p>
 *  <p>21 = Sports</p>
 *  <p>22 = Geography</p>
 *  <p>23 = History</p>
 *  <p>24 = Politics</p>
 *  <p>25 = Art</p>
 *  <p>26 = Celebrities</p>
 *  <p>27 = Animals</p>
 *  <p>28 = Vehicles</p>
 *  <p>29 = Entertainment: Comics</p>
 *  <p>30 = Science: Gadgets</p>
 *  <p>31 = Entertainment: Japanese Anime & Manga</p>
 *  <p>32 = Entertainment: Cartoon & Animations</p>
 */

public class Category {

    private int id;
    private String name;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

}
