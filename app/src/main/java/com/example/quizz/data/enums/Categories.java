package com.example.quizz.data.enums;
/**
 *  Enum storing all available categories:
 *  <p>9 = General Knowledge</p>
 *  <p>10 = Entertainment: Books</p>
 *  <p>11 = Entertainment: Film</p> todo !!!!!!!
 *  <p>12 = Entertainment: Music</p> todo!!!!!
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
public enum Categories {

    GENERALKNOWLEDGE("General Knowledge", 9),
    BOOKS("Entertainment: Books", 10),
    FILM("Entertainment: Film", 11),
    MUSIC("Entertainment: Music", 12),
    MUSICALS("Entertainment: Musicals & Theatres", 13),
    TV("Entertainment: Television", 14),
    VIDEOGAMES("Entertainment: Video Games", 15),
    BOARDGAMES("Entertainment: Board Games", 16),
    NATURE("Science & Nature", 17),
    COMPUTERS("Science: Computers", 18),
    MATHEMATICS("Science: Mathematics", 19),
    MYTHOLOGY("Mythology", 20),
    SPORTS("Sports", 21),
    GEOGRAPHY("Geography", 22),
    HISTORY("History", 23),
    POLITICS("Politics", 24),
    ART("Art", 25),
    CELEBRITIES("Celebrities", 26),
    ANIMALS("Animals", 27),
    VEHICLES("Vehicles", 28),
    COMICS("Entertainment: Comics", 29),
    GADGETS("Science: Gadgets", 30),
    ANIME("Entertainment: Japanese Anime & Manga", 31),
    CARTOONANIMATIONS("Entertainment: Cartoon & Animations", 32);



    private final String name;
    private final int id;

    Categories(String name, int id){
        this.name = name;
        this.id = id;
    }

    public int getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }


}

