package com.example.quizz.questionManager;

import com.example.quizz.data.gameData.QuestionData;
import com.example.quizz.exceptions.QueryException;
import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * {@code QuestionManager} manages all API calls
 * and data types.
 */
public interface IQuestionManager {
    /**
     * @param number checks if the number is valid.
     * @return returns true if valid, false if not.
     */
    public boolean checkNumber(int number);
    /**
     * This method decides if the URL String is
     * concatenated with the category. If this method
     * returns false, the category will not be
     * specified, so it will just give out random
     * category-Questions.
     */
    public boolean checkCategory(int categoryId) throws IOException;
    /**
     * @param difficulty checks if the String is valid.
     * @return returns true if valid, false if not.
     */
    public boolean checkDifficulty(String difficulty);
    /**
     * @param type checks if the String is valid.
     * @return returns true if valid, false if not.
     */
    public boolean checkType(String type);
    public URL createURL(int number, int category, String difficulty,
                   String type) throws QueryException, IOException;
    /**
     * Converts an URL and returns a jsonString
     */
    public String convertURL(URL u) throws IOException;
    /**
     * Uses parameters to create a specified API query.
     */
    public List<Question> getApiData(int questionNumber,
              int categoryId, String difficulty, String type)
                        throws IOException, QueryException;
    /**
     * Converts the jsonString to the {@link QuestionData} object.
     */
    public QuestionData convertJsonString(String jsonString);
    /**
     * Gets response code to corresponding data
     */
    public int checkResponseCode(QuestionData data);
}
