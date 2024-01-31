package com.paulorodrigues.authentication.commons.util;

public class ConstantsUtil {

    public static final int MAX_SIZE_NAME = 100;
    public static final int MAX_SIZE_SHORT_TEXT = 200;
    public static final int MAX_SIZE_LONG_TEXT = 600;

    public static final int MAX_SIZE_ADDRESS_NUMBER = 9;
    public static final int MAX_SIZE_ADDRESS_CEP = 8;
    public static final int MAX_SIZE_ADDRESS_ZIPCODE = 12;
    public static final int MAX_SIZE_ADDRESS_COORDINATION = 20;

    public static final int MAX_SIZE_CNPJ = 14;
    public static final int MAX_SIZE_CPF = 11;

    public static final String AUTH_BASE_API = "/com/paulorodrigues/authentication/api";
    public static final String ADDRESSES_V1_BASE_API = AUTH_BASE_API + "/v1/addresses";

    public static final String DELETE_PATH = "/{id}";
    public static final String FIND_ALL_PATH = "/all";
    public static final String FIND_BY_ID_PATH = "/{id}";
    public static final String FIND_BY_NAME_PATH = "/fetch/{name}";
    public static final String FIND_BY_NAME_PAGEABLE_PATH = "/fetch/name";
    public static final String FIND_PAGEABLE_PATH = "/fetch";
    public static final String GET_LOGRADOUROS = "/logradouros";

    public static final String GET_CITIES_PATH = "/{country}/{state}/cities";
    public static final String GET_STATES_PATH = "/{country}/states";
    public static final String GET_COUNTRIES_PATH = "/countries";
    public static final String PEOPLE_V1_BASE_API = AUTH_BASE_API + "/v1/people";
    public static final String UPDATE_PATH = "/{id}";
    public static final String USERS_V1_BASE_API = AUTH_BASE_API + "/v1/users";
    public static final String GET_BY_NAME_PATH = "";
    public static final String GET_BY_ID_PATH = "";
    public static final String UPDATE_USER_PATH = "";
    public static final String GET_ROLES_PATH = "";
}
