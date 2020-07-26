import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

class JSONSyntaxException extends Exception {

    public JSONSyntaxException() {
        super();
    }
}


public abstract class Parser {

    /**
     * <p>Parses out a JSON array string</p>
     * @param name the name of the array in the JSON
     * @param json a JSON string to be parsed
     * @return a JSONEntity containing the parsed array
     * @throws JSONSyntaxException on incorrect syntax
     */
    private static JSONEntity parseArray(String name, String json) throws JSONSyntaxException{

        ArrayList<Object> array = new ArrayList<>();
        // Sounds good, doesn't work in case of array of arrays or array of objects
        //String[] tokens = json.split(",");

        // Tokenize the json string
        ArrayList<String> tokens = new ArrayList<>();
        int startIndex = 0, stopIndex;
        for(;startIndex < json.length(); startIndex++) {
            if(json.charAt(startIndex) == ' ') continue;

            stopIndex = json.indexOf(",", startIndex);
            if(stopIndex != -1)
                tokens.add(json.substring(startIndex, stopIndex));
            else
                tokens.add(json.substring(startIndex));
        }

        // Trim all of the tokens
        tokens.stream().map(String::trim).collect(Collectors.toList()).toArray();

        // It can very well be an array of objects or arrays
        for(String token : tokens) {
            // Check if object
            if(token.charAt(0) == '{') {
                // Check if valid object
                if(token.charAt(token.length()-1) != '}')
                    throw new JSONSyntaxException();
                array.add(parseObject(token.substring(1, token.length()-1)));
                continue;
            }
            // Check if array
            else if (token.charAt(0) == '[') {
                // Check if valid array
                if (token.charAt(token.length() - 1) != ']')
                    throw new JSONSyntaxException();
                array.add(parseArray(null, token.substring(1, token.length()-1)));
                continue;
            }

            try {
                Long value = Long.parseLong(token);
                array.add(value);
                continue;
            } catch (NumberFormatException e) {}
            try {
                Double value = Double.parseDouble(token);
                array.add(value);
                continue;
            } catch (NumberFormatException e) {}

            if(token.equals("true"))
                array.add(true);
            else if(token.equals("false"))
                array.add(false);
            else if(token.equals("null"))
                array.add(null);
            else {
                // Check whether the value is a valid string, i.e. is enquote
                if(token.charAt(0) != '"' || token.charAt(token.length()-1) != '"')
                    throw new JSONSyntaxException();

                array.add(token.substring(1, token.length()-1));
            }
        }
        return Wrapper.JSONArray(name, array);
    }

    /**
     * <p>Overloaded from <i>parseObject(String name, String json)</i></p>
     * <p>Public entry point for the parser</p>
     * @param json a JSON string to be parsed, <b><u>without curved brackets</u></b>
     * @return a JSONEntity containing the parsed object
     * @throws JSONSyntaxException on incorrect syntax
     */
    public static JSONEntity parseObject(String json) throws JSONSyntaxException {
        return parseObject(null, json);
    }

    /**
     * <p>Parses out a JSON string into an object</p>
     * @param name the name of the object in the parent object
     * @param json a JSON string to be parsed, <b><u>without curved brackets</u></b>
     * @return a JSONEntity containing the parsed object
     * @throws JSONSyntaxException on incorrect syntax
     */
    private static JSONEntity parseObject(String name, String json) throws JSONSyntaxException {

        /*
        Parsing sequence:
        1. If encountered a double quote, locate the next one and fetch the name in between.
        2. Locate a column.
           2.1. If after a column comes an opening square bracket, locate the matching closing bracket and parse the array.
           2.2. If after a column comes an opening curly bracket, locate the matching closing bracket and parse the object.
           2.3. If the column is not found, throw IllegalArgumentException("Invalid JSON");
        3. Locate the comma. If a comma or a closing bracket is not found, the json is invalid.
           If an array or an object has not been detected, do the following:
           3.1. Try to parse an integer value, JSONNumber() on success
           3.2. Try to parse a double value, JSONNumber() on success
           3.3. Try to parse a boolean value, JSONBoolean() on success
           3.4. Try to parse a null value, JSONNull() on success
           3.5. JSONString()
        4. Loop
         */

        int startIndex = 0, stopIndex;
        ArrayList<JSONEntity> object = new ArrayList<>();
        String valueName;
        for(; startIndex < json.length(); startIndex++) {

            if(json.charAt(startIndex) == '"') {

                // Find the end of the string
                stopIndex = json.indexOf("\"", startIndex);

                // Fetch the string without quotation symbols
                valueName = json.substring(startIndex + 1, stopIndex);

                // Find the beginning of the value (:)
                startIndex = stopIndex + 1;
                for(;;startIndex++) {
                    if(json.charAt(startIndex) == ':') break;

                    if(json.charAt(startIndex) == '"') throw new JSONSyntaxException();
                }

                // Find the end of the value (, or end of string)
                boolean eof = true;
                for(stopIndex = startIndex; stopIndex < json.length(); stopIndex++) {
                    if(json.charAt(stopIndex) == ',') {
                        eof = false;
                        break;
                    }

                    if(json.charAt(stopIndex) == '"') throw new JSONSyntaxException();
                }

                // If we've found the comma, we don't need it - throw it away
                // If we've not found the comma, we need the last character - not throw it away
                String value = eof ? json.substring(startIndex) : json.substring(startIndex, stopIndex);

                // Remove the whitespaces
                value = value.trim();

                // Parse the value
                if(value.charAt(0) == '{') {
                    // Check if valid object
                    if(value.charAt(value.length()-1) != '}')
                        throw new JSONSyntaxException();
                    object.add(parseObject(valueName, value.substring(1, value.length()-1)));
                    continue;
                }
                // Check if array
                else if (value.charAt(0) == '[') {
                    // Check if valid array
                    if (value.charAt(value.length() - 1) != ']')
                        throw new JSONSyntaxException();
                    object.add(parseArray(valueName, value.substring(1, value.length()-1)));
                    continue;
                }

                try {
                    Long number = Long.parseLong(value);
                    object.add(Wrapper.JSONNumber(valueName, number));
                    continue;
                } catch (NumberFormatException e) {}
                try {
                    Double number = Double.parseDouble(value);
                    object.add(Wrapper.JSONNumber(valueName, number));
                    continue;
                } catch (NumberFormatException e) {}

                if(value.equals("true"))
                    object.add(Wrapper.JSONBoolean(valueName, true));
                else if(value.equals("false"))
                    object.add(Wrapper.JSONBoolean(valueName, false));
                else if(value.equals("null"))
                    object.add(Wrapper.JSONNull(valueName));
                else {
                    // Check whether the value is a valid string, i.e. is enquote
                    if(value.charAt(0) != '"' || value.charAt(value.length()-1) != '"')
                        throw new JSONSyntaxException();

                    object.add(Wrapper.JSONString(valueName, value.substring(1, value.length()-1)));
                }
            }

        }
        return Wrapper.JSONObject(name, object);
    }

}
