public class Main {

    public static void main(String[] args) {

        if(args.length < 2) {
            System.err.println("No JSON string provided");
            return;
        }

        try {
            String json = args[1];
            if (json.charAt(0) != '{' || json.charAt(json.length()-1) != '}') {
                System.err.println("Invalid JSON");
                return;
            }

            // Remove the brackets
            json = json.substring(1, json.length()-1);

            // Parse the JSON
            Object parsedJson = Parser.parseObject(json);

            // Output the parsed JSON
            System.out.println(parsedJson.toString());

        } catch (JSONSyntaxException e)
        {
            System.err.println("Invalid JSON");
        }

    }

}
