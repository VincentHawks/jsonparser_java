import java.util.ArrayList;

public abstract class Wrapper {

    /**
     * <p>Construct a JSON null value wrapper</p>
     * @return anonymous class implements JSONEntity
     */
    public static JSONEntity JSONNull(String name)
    {
        return new JSONEntity() {
            @Override
            public Object value() {
                return null;
            }

            @Override
            public String name() {
                return name;
            }

            @Override
            public Class type() {
                return null;
            }

            @Override
            public String toString() {return "\"" + this.name() + "\": Null";}
        };
    }

    /**
     * <p>Construct a JSON string value wrapper</p>
     * @param name variable name
     * @param value assigned value
     * @return anonymous class implements JSONEntity
     */
    public static JSONEntity JSONString(String name, String value)
    {
        return new JSONEntity() {
            @Override
            public Object value() {
                return value;
            }

            @Override
            public String name() {
                return name;
            }

            @Override
            public Class type() {
                return String.class;
            }

            @Override
            public String toString() {
                return "\"" + this.name() + "\": \"" + this.value() + "\"";
            }
        };
    }

    /**
     * <p>Constructs a JSON boolean value wrapper</p>
     * @param name variable name
     * @param value assigned value
     * @return anonymous class implements JSONEntity
     */
    public static JSONEntity JSONBoolean(String name, Boolean value)
    {
        return new JSONEntity() {
            @Override
            public Object value() {
                return value;
            }

            @Override
            public String name() {
                return name;
            }

            @Override
            public Class type() {
                return Boolean.class;
            }

            @Override
            public String toString() {
                return "\"" + this.name() + "\": " + this.value() + "\"";
            }
        };
    }

    /**
     * <p>Constructs a JSON number (integer) value wrapper</p>
     * @param name variable name
     * @param value assigned value
     * @return anonymous class implements JSONEntity
     */
    public static JSONEntity JSONNumber(String name, Long value)
    {
        return new JSONEntity() {
            @Override
            public Object value() {
                return value;
            }

            @Override
            public String name() {
                return name;
            }

            @Override
            public Class type() {
                return Integer.class;
            }

            @Override
            public String toString() {
                return "\"" + this.name() + "\": " + this.value() + "\"";
            }
        };
    }

    /**
     * <p>Constructs a JSON number (floating point) value wrapper</p>
     * @param name variable name
     * @param value assigned value
     * @return anonymous class implements JSONEntity
     */
    public static JSONEntity JSONNumber(String name, Double value)
    {
        return new JSONEntity() {
            @Override
            public Object value() {
                return value;
            }

            @Override
            public String name() {
                return name;
            }

            @Override
            public Class type() {
                return Double.class;
            }

            @Override
            public String toString() {
                return "\"" + this.name() + "\": " + this.value() + "\"";
            }
        };
    }

    /**
     * <p>Constructs a JSON array value wrapper</p>
     * <p>If a number is pushed to the array, it must be first attempted to be cast to Integer,
     * and then pushed as a Double if failed. Arrays can contain different types at the same time, hence Object</p>
     * @param name variable name
     * @param value ArrayList of Object
     * @return anonymous class implements JSONEntity
     */
    public static JSONEntity JSONArray(String name, ArrayList<Object> value)
    {
        return new JSONEntity() {
            @Override
            public Object value() {
                return value;
            }

            @Override
            public String name() {
                return name;
            }

            @Override
            public Class type() {
                return ArrayList.class;
            }

            @Override
            public String toString() {

                String output = "\"" + this.name() + "\": [\n";
                ArrayList<Object> array = (ArrayList<Object>) this.value();

                for(int i = 0; i < array.size() - 1; i++) {
                    output += array.get(i).toString() + ",\n";
                }
                output += array.get(array.size()-1) + "\n]";
                return output;
            }
        };
    }

    /**
     * <p>Constructs a JSON object value wrapper</p>
     * @param name variable name
     * @param value ArrayList of JSONEntity
     * @return anonymous class implements JSONEntity
     */
    public static JSONEntity JSONObject(String name, ArrayList<JSONEntity> value)
    {
        return new JSONEntity() {
            @Override
            public Object value() {
                return value;
            }

            @Override
            public String name() {
                return name;
            }

            @Override
            public Class type() {
                return Object.class;
            }

            @Override
            public String toString() {

                String output = "\"" + this.name() + "\": {\n";
                ArrayList<JSONEntity> array = (ArrayList<JSONEntity>) this.value();

                for(int i = 0; i < array.size() - 1; i++) {
                    output += array.get(i).toString() + ",\n";
                }
                output += array.get(array.size()-1) + "\n}";
                return output;
            }
        };
    }

}
