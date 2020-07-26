public interface JSONEntity {

    /**
     *  @return the stored value
     */
    Object value();


    /**
     *  @return the variable name
     */
    String name();

    /**
     *  @return the stored value datatype
     */
    Class type();

}
