package com.example.quizz.questionManager;

import com.example.quizz.data.gameData.Categories;
import com.example.quizz.data.gameData.Difficulties;
import com.example.quizz.data.gameData.QuestionData;
import com.example.quizz.exceptions.QueryException;
import com.google.gson.Gson;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import android.util.Base64;
import java.util.List;
import static java.lang.String.valueOf;



/**
 * {@code QuestionManager} manages all API calls and data types.
 */

//TODO: Decoden von Sonderzeichen. !!!!!

public class QuestionManager implements IQuestionManager {

    private final Gson gson = new Gson();


    public QuestionManager(){

    }


    /**
     * @param number checks if the number is valid.
     * @return returns true if valid, false if not.
     */
    public boolean checkNumber(int number){
        return 1 <= number && number <= 50;
    }

    /**
     * This method decides if the URL String is concatenated with the category. If this method returns false,
     * the category will not be specified, so it will just give out random category-Questions.
     * @param categoryId checks if the given categoryId is valid/existing.
     * @return returns true if valid, false if not.
     */
    public boolean checkCategory(int categoryId) throws IOException {
        if(categoryId == -1) return false;
        for (Categories categories : Categories.values()) {
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
     * @return the String of the token.
     * @throws IOException, if the object doesn't fit the gson.
     */
    public String getNewToken() throws IOException {          //generates a new Token.
        URL requestToken = new URL(TriviaURL.NEWTOKEN.getLink());

        String json = convertURL(requestToken);

        SessionToken session = gson.fromJson(json, SessionToken.class);

        return session.getToken();
    }

    /**
     * Uses parameters to create a specified API query.
     * @param questionNumber indicates how many {@link Question}'s are requested.
     * @param categoryId indicates the {@link Categories} of the Questions, in form of the CategoryId. Check if the id is correct: {@link #checkCategory(int)}.
     * @param difficulty indicates the difficulty. Just use {@link Difficulties#getName()} to transfer the String needed.
     * @param type indicates the type of a quesiton. Use {@link Difficulties#getName()} like above.
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
        return decodeBase64(exampleList);
    }

    /**
     * Due to the fact, that the API can't show special characters properly, an encoded API call is necessary. To decode them this
     * method will make use of the {@link java.util.Base64} library. Due to the fact, that only the results but not the
     * variable names provided by the json file are encoded, we need to decode each object individually
     * (e.g. a part in the json file can look like this -> "difficulty":"ZWFzeQ==") Only the second part needs to be decoded, not the first. "ZWFzeQ==" gets decoded to "easy".
     * @param qList takes the temporary List<Question> exampleList from {@link #getApiData(int, int, String, String)} and decodes it's Strings with the Base64 decoding scheme.
     * @return the decoded List<Question>.
     */
    public List<Question> decodeBase64(List<Question> qList){

        for(Question q : qList){

            byte[] decodedQuestion = android.util.Base64.decode(q.getQuestion(), Base64.DEFAULT);
            q.setQuestion(new String (decodedQuestion));

            byte[] decodedCorrAns = Base64.decode(q.getCorrect_answer(), Base64.DEFAULT);
            q.setCorrect_answer(new String(decodedCorrAns));

            List<String> tempIncorrectAns = q.getIncorrect_answers();
            for(int i = 0; i < tempIncorrectAns.size(); i++){
                byte[] decodedIncorrAns = Base64.decode(q.getIncorrect_answers().get(i), Base64.DEFAULT);
                tempIncorrectAns.set(i, new String(decodedIncorrAns));
            }
            q.setIncorrect_answers(tempIncorrectAns);


            byte[] decodedType = Base64.decode(q.getTypeString(), Base64.DEFAULT);
            q.setType(new String(decodedType));

            byte[] decodedDiff = Base64.decode(q.getDifficultyString(), Base64.DEFAULT);
            q.setDifficulty(new String(decodedDiff));

            byte[] decodedCat = Base64.decode(q.getCategoryString(), Base64.DEFAULT);
            q.setCategory(new String(decodedCat));

        }

        return qList;
    }
    //todo whitespaces mit trim(deprecated) oder strip(aktueller). Evtl noch special split(regular expression) -> prog3 beleg


    /**
     * Creates the URL of the request. This method will then get used in the {@link #getApiData(int, int, String, String)} method.
     * @param number Amount of {@link Question} requested. Needs to be 1 <= number <= 50. Needs to be specified.
     * @param category ID of the {@link Categories} requested. -1 for no specification needed.
     * @param difficulty as String. "easy", "medium", "hard". "" for no specification.
     * @param type as String. "multiple" or "boolean". "" for no specification.
     * @return the specified URL including the requested attributes plus a new generated {@link SessionToken}.
     * @throws QueryException throws, when the generated URL is not correct, which is the case when the requested amount of questions is below 1 or above 50.
     * @throws IOException throws, when an I/O error occurs using the BufferedReader.
     */
    public URL createURL(int number, int category, String difficulty, String type) throws QueryException, IOException {
        String queryUrl;

        if (!checkNumber(number)){
            throw new QueryException("Amount of questions, needs to be higher than 0 and less than 51");
        }
        queryUrl = TriviaURL.BASICURL.getLink();

        String amount = valueOf(number);
        String categories = "&category=";
        String difficulties = "&difficulty=";
        String types = "&type=";
        String decodeBase64 = "&encode=base64";     //change if other encoding protocol should be used
        String token = "&token=";


        queryUrl = queryUrl.concat(amount);

        if (checkCategory(category)) {
            categories = categories.concat(valueOf(category));
            queryUrl = queryUrl.concat(categories);
        }
        if (checkDifficulty(difficulty)) {
            difficulties = difficulties.concat(difficulty);
            queryUrl = queryUrl.concat(difficulties);
        }
        if (checkType(type)) {
            types = types.concat(type);
            queryUrl = queryUrl.concat(types);
        }

        queryUrl = queryUrl.concat(decodeBase64);   //concat the URL decoding String

        token = token.concat(getNewToken());
        queryUrl = queryUrl.concat(token);

        return new URL(queryUrl);
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
     * @return {@link QuestionData} object, storing informations from the jsonString.
     */
    public QuestionData convertJsonString(String jsonString) {                   // jsonString -> Question Objekt
        return gson.fromJson(jsonString, QuestionData.class);
    }

    /**
     * Gets response code to corresponding data
     * @return the responseCode of the data as Integer.
     */
    public int checkResponseCode(QuestionData data) {
        return data.getResponse_code();
    }

    /**
     * Gets a List<{@link Question}> from a given {@link QuestionData} object.
     * @return the List<Player>.
     */
    public List<Question> getQuestions (QuestionData data) {
        return data.getResults();
    }


}
