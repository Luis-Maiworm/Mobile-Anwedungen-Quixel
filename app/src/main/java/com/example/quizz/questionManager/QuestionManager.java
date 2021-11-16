package com.example.quizz.questionManager;

import com.example.quizz.exceptions.QueryException;
import com.google.gson.Gson;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import static java.lang.String.valueOf;


/**
 * {@code QuestionManager} handelt alle Anfragen an die API und managet die einzelnen Datentypen.
 */

//TODO: Decoden von Sonderzeichen.

public class QuestionManager {

    private final Gson gson = new Gson();
    public List<Category> categoryList = new ArrayList<>();
    private final String categorie_api = "data/category.json";

    public String url;
    private final String resetToken = "https://opentdb.com/api_token.php?command=reset&token=";
    private final String requestCategories = "https://opentdb.com/api_category.php";


    //todo remove: nur für test zwecke!
    public String testUrl = "";
    public String getTestUrl(){
        return this.testUrl;
    }




    /**
     * Get the categorly list from a saved json file
     * @return a {@link QuestionData} object, which stores every {@link Category} name and id.
     * @throws IOException
     */
    public QuestionData categoryFromJson() throws IOException {
        return gson.fromJson(new FileReader(new File(categorie_api)), QuestionData.class);
    }

    /**
     * Get the category list from a URL
     * @return a {@link QuestionData} object, which stores every {@link Category} name and id.
     * @throws IOException, if the object structure doesn't fit the given json file.
     */
    public QuestionData categoryFromJsonUrl() throws IOException {
        URL questionURL = new URL(requestCategories);
        return gson.fromJson(convertURL(questionURL), QuestionData.class);
    }

    /**
     *
     * @return List<Category> uses the {@link #categoryFromJson()} method, to return the
     * Categories as a List, not a {@link QuestionData} object.
     * @throws IOException, if the object structure doesn't fit the given json file.
     */
    public List<Category> getCategory() throws IOException {
        return categoryFromJson().getTrivia_categories();
    }

    /**
     * @param number checks if the number is valid.
     * @return returns true if valid, false if not.
     */
    public boolean checkNumber(int number){
        return 1 <= number && number <= 50;
    }

    /**
     * @param categoryId checks if the given categoryId is valid/existing.
     * @return returns true if valid, false if not.
     */
    public boolean checkCategory(int categoryId) throws IOException {
        if(categoryId == -1) return false;
        // categoryList = categoryFromJson().getTrivia_categories();
        categoryList = categoryFromJsonUrl().getTrivia_categories();
        for (Category categories : categoryList) {
            if (categories.getId() == categoryId) return true;
        }
        return false;
    }

    /**
     * @param difficulty checks if the String is valid.
     * @return returns true if valid, false if not.
     */
    public boolean checkDifficulty(String difficulty){
        if(difficulty.isEmpty()) return false;
        return difficulty.equals("easy") || difficulty.equals("medium") || difficulty.equals("hard");
    }

    /**
     * @param type checks if the String is valid.
     * @return returns true if valid, false if not.
     */
    public boolean checkType(String type){
        if(type.isEmpty()) return false;
        return type.equals("multiple") || type.equals("boolean") || type.equals("");
    }

    /**
     * Uses parameters to create a specified API query.
     * @param questionNumber indicates how many {@link Question}'s are requested.
     * @param categoryId indicates the {@link Category} of the Questions, in form of the CategoryId. Check if the id is correct: {@link #checkCategory(int)}.
     * @param difficulty indicates the difficulty as a String ("easy", "medium", "hard" or "").
     * @param type indicates the type as a String ("boolean", "multiple" or "").
     * @return a List<{@link Question}>, with Questions that fit ur query.
     * @throws IOException, if the object structure of the recipient object (in this case {@link QuestionData} is valid with the json format
     * @throws QueryException, if the URL being generated is not correct.
     */
    public List<Question> getApiData(int questionNumber, int categoryId, String difficulty, String type) throws IOException, QueryException {

        List<Question> exampleList = new ArrayList<>();
        QuestionData temp = convertJsonString(convertURL(createURL(questionNumber, categoryId, difficulty, type)));

        switch(checkResponseCode(temp)) {
            case 0: exampleList = getQuestions(temp); break;
            case 1: throw new QueryException("No Results for the query");
            case 2: throw new QueryException("Invalid Parameters in URL");
            case 3: throw new QueryException("Token not found - Doesn't exist");
            case 4: throw new QueryException("Token is empty, all possible questions for this query have been returnt");
        }
        return exampleList;
    }

    /**
     * Creates the URL of the request. This method will then get used in the {@link #getApiData(int, int, String, String)} method.
     * @param number Amount of {@link Question} requested. Needs to be 1 <= number <= 50. Needs to be specified.
     * @param category ID of the {@link Category} requested. -1 for no specification needed.
     * @param difficulty as String. "easy", "medium", "hard". "" for no specification.
     * @param type as String. "multiple" or "boolean". "" for no specification.
     * @return the specified URL including the requested attributes plus a new generated {@link SessionToken}.
     * @throws QueryException throws, when the generated URL is not correct, which is the case when the requested amount of questions is below 1 or above 50.
     * @throws IOException throws, when an I/O error occurs using the BufferedReader.
     */
    public URL createURL(int number, int category, String difficulty, String type) throws QueryException, IOException {

        if (!checkNumber(number)){
            throw new QueryException("Amount of questions, needs to be higher than 0 and less than 51");      //TODO Message?
        }
        url = "https://opentdb.com/api.php?amount=";
        String amount = valueOf(number);
        String categories = "&category=";
        String difficulties = "&difficulty=";
        String types = "&type=";
        String token = "&token=";

        url = url.concat(amount);

        if (checkCategory(category)) {
            categories = categories.concat(valueOf(category));
            url = url.concat(categories);
        }
        if (checkDifficulty(difficulty)) {
            difficulties = difficulties.concat(difficulty);
            url = url.concat(difficulties);
        }
        if (checkType(type)) {
            types = types.concat(type);
            url = url.concat(types);
        }
        token = token.concat(getNewToken());
        url = url.concat(token);

        testUrl = url;
        return new URL(url);
    }



    /**
     * Session Tokens are unique keys that will help keep track of the questions the
     * API has already retrieved. By appending a Session Token to a API Call,
     * the API will never give you the same question twice. Over the lifespan of a Session Token,
     * there will eventually reach a point where you have exhausted all the possible questions in the database.
     * At this point, the API will respond with the approperate "Response Code". From here, you can either "Reset" the Token,
     * which will wipe all past memory, or you can ask for a new one.
     * Session Tokens will be deleted after 6 hours of inactivity.
     *
     *
     * The API appends a "Response Code" to each API Call to help tell developers what the API is doing.
     *
     * <p>Code 0: Success Returned results successfully.</p>
     * <p>Code 1: No Results Could not return results. The API doesn't have enough questions for your query. (Ex. Asking for 50 Questions in a Category that only has 20.)</p>
     * <p>Code 2: Invalid Parameter Contains an invalid parameter. Arguements passed in aren't valid. (Ex. Amount = Five)</p>
     * <p>Code 3: Token Not Found Session Token does not exist.</p>
     * <p>Code 4: Token Empty Session Token has returned all possible questions for the specified query. Resetting the Token is necessary.</p>
     *
     *
     */

    //TODO Abspeicherung von Token - und Code Auswertung = evtl. resetten

    /**
     * Generates a new {@link SessionToken}.
     * @returns the String of the token.
     * @throws IOException, if the object doesn't fit the gson.
     */
    public String getNewToken() throws IOException {          //generates a new Token.
        URL requestToken = new URL("https://opentdb.com/api_token.php?command=request");

        String json = convertURL(requestToken);

        SessionToken session = gson.fromJson(json, SessionToken.class);

        return session.getToken();
    }

    /**
     * Converts an URL and returns a jsonString
     * @param u URL that gets converted, will be generated by the {@link #createURL(int, int, String, String)} method.
     * @return the jsonString.
     * @throws IOException, if an I/O error occurs, while reading the file at URL.
     */
    public String convertURL(URL u) throws IOException {                             //URL -> jsonString
        BufferedReader in = new BufferedReader(new InputStreamReader((u.openStream())));
        String jsonString = in.readLine();
        in.close();

        return jsonString;
    }

    /**
     * Converts the jsonString to the {@link QuestionData} object.
     * @param jsonString will be generated from the {@link #convertURL(URL)} method.
     * @returns {@link QuestionData} object, storing informations from the jsonString.
     */
    public QuestionData convertJsonString(String jsonString) {                   // jsonString -> Question Objekt
        return gson.fromJson(jsonString, QuestionData.class);
    }

    /**
     * Gets response code to corresponding data
     * @returns the responseCode of the data as Integer.
     */
    public int checkResponseCode(QuestionData data) {
        return data.getResponse_code();
    }

    /**
     * Gets a List<{@link Question}> from a given {@link QuestionData} object.
     * @returns the List<Player>.
     */
    public List<Question> getQuestions (QuestionData data) {
        return data.getResults();
    }


}
