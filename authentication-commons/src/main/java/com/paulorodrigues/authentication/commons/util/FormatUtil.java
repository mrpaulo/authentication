package com.paulorodrigues.authentication.commons.util;

import com.paulorodrigues.authentication.commons.model.UserQuery;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

public class FormatUtil {

    public static String getUsernameLogged() {
        String userName = "";
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth != null){
            //get the email address
            userName = auth.getName();
        }
        return userName;
    }

    public static StringBuilder removeLastComma(StringBuilder sb) {
        int length = sb.length();
        if (length > 2 && sb.charAt(length - 2) == ',' && sb.charAt(length - 1) == ' ') {
            sb.setLength(length - 2);
        }
        return sb;
    }

    public  static StringBuilder printUpdateControl(StringBuilder sb, LocalDate createAt, String createBy, LocalDate updateAt, String updateBy){
        if (createAt != null) {
            sb.append("createAt=").append(createAt).append(", ");
        }
        if (createBy != null && !createBy.isEmpty()) {
            sb.append("createBy='").append(createBy).append('\'').append(", ");
        }
        if (updateAt != null) {
            sb.append("updateAt=").append(updateAt).append(", ");
        }
        if (updateBy != null && !updateBy.isEmpty()) {
            sb.append("updateBy='").append(updateBy).append('\'').append(", ");
        }
        return sb;
    }

    public static boolean isEmptyOrNull(String value) {
        return value == null || value.isEmpty();
    }

    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static Pageable buildPageable(PageableQuery pageableQuery){
        return PageRequest.of(pageableQuery.getCurrentPage() > 0 ? pageableQuery.getCurrentPage() - 1 : 0, pageableQuery.getRowsPerPage(), Sort.by(pageableQuery.getSortColumn()));
    }

    public static boolean isCPF(String CPF) {
        // considera-se erro CPF's formados por uma sequencia de numeros iguais
        if (CPF.equals("00000000000")
                || CPF.equals("11111111111")
                || CPF.equals("22222222222") || CPF.equals("33333333333")
                || CPF.equals("44444444444") || CPF.equals("55555555555")
                || CPF.equals("66666666666") || CPF.equals("77777777777")
                || CPF.equals("88888888888") || CPF.equals("99999999999")
                || (CPF.length() != 11)) {
            return (false);
        }

        char dig10, dig11;
        int sm, i, r, num, peso;

        // "try" - protege o codigo para eventuais erros de conversao de tipo (int)
        try {
            // Calculo do 1o. Digito Verificador
            sm = 0;
            peso = 10;
            for (i = 0; i < 9; i++) {
                // converte o i-esimo caractere do CPF em um numero:
                // por exemplo, transforma o caractere '0' no inteiro 0
                // (48 eh a posicao de '0' na tabela ASCII)
                num = (int) (CPF.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11)) {
                dig10 = '0';
            } else {
                dig10 = (char) (r + 48); // converte no respectivo caractere numerico
            }
            // Calculo do 2o. Digito Verificador
            sm = 0;
            peso = 11;
            for (i = 0; i < 10; i++) {
                num = (int) (CPF.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11)) {
                dig11 = '0';
            } else {
                dig11 = (char) (r + 48);
            }

            // Verifica se os digitos calculados conferem com os digitos informados.
            if ((dig10 == CPF.charAt(9)) && (dig11 == CPF.charAt(10))) {
                return (true);
            } else {
                return (false);
            }
        } catch (InputMismatchException erro) {
            return (false);
        }
    }

    public static String removeFormatCPF(String CPF) {
        if(isEmptyOrNull(CPF)){
            return null;
        }
        return CPF.replace(".", "").replace("-", "");
    }

    /**
     * Formata um CNPJ
     *
     * @param cnpj	String contendo o CNPJ não formatado
     * @return string	String contendo o CNPJ formatado
     */
    public static String formatCnpj(String cnpj) {
        if (cnpj != null) {
            cnpj = cnpj.trim();
            String result = cnpj;
            if (cnpj.length() == 14) {
                result = cnpj.substring(0, 2) + "."
                        + cnpj.substring(2, 5) + "."
                        + cnpj.substring(5, 8) + "/"
                        + cnpj.substring(8, 12) + "-"
                        + cnpj.substring(12, 14);
            }

            if (cnpj.length() == 15) {
                result = cnpj.substring(0, 3) + "."
                        + cnpj.substring(3, 6) + "."
                        + cnpj.substring(6, 9) + "/"
                        + cnpj.substring(9, 13) + "-"
                        + cnpj.substring(13, 15);
            }

            return result;
        } else {
            return "";
        }
    }

    /**
     * Retorna o CNPJ não formatado
     *
     * @param cnpj	String contendo o CNPJ formatado
     * @return string	String contendo o CNPJ não formatado
     */
    public static String desformatCnpj(String cnpj) {
        StringBuilder newCNPJ = new StringBuilder();

        if (cnpj == null) {
            return cnpj;
        }

        char[] val = cnpj.toCharArray();
        for (int i = 0; i < val.length; i++) {
            if ((val[i] >= '0') && (val[i] <= '9')) {
                newCNPJ.append(val[i]);
            }
        }

        if (newCNPJ.toString().length() == 14) {
            return newCNPJ.toString();
        } else {
            return cnpj;
        }
    }

    public static boolean isCNPJ(String CNPJ) {
// considera-se erro CNPJ's formados por uma sequencia de numeros iguais
        if (CNPJ.equals("00000000000000") || CNPJ.equals("11111111111111")
                || CNPJ.equals("22222222222222") || CNPJ.equals("33333333333333")
                || CNPJ.equals("44444444444444") || CNPJ.equals("55555555555555")
                || CNPJ.equals("66666666666666") || CNPJ.equals("77777777777777")
                || CNPJ.equals("88888888888888") || CNPJ.equals("99999999999999")
                || (CNPJ.length() != 14)) {
            return (false);
        }

        char dig13, dig14;
        int sm, i, r, num, peso;

// "try" - protege o código para eventuais erros de conversao de tipo (int)
        try {
// Calculo do 1o. Digito Verificador
            sm = 0;
            peso = 2;
            for (i = 11; i >= 0; i--) {
// converte o i-ésimo caractere do CNPJ em um número:
// por exemplo, transforma o caractere '0' no inteiro 0
// (48 eh a posição de '0' na tabela ASCII)
                num = (int) (CNPJ.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso + 1;
                if (peso == 10) {
                    peso = 2;
                }
            }

            r = sm % 11;
            if ((r == 0) || (r == 1)) {
                dig13 = '0';
            } else {
                dig13 = (char) ((11 - r) + 48);
            }

// Calculo do 2o. Digito Verificador
            sm = 0;
            peso = 2;
            for (i = 12; i >= 0; i--) {
                num = (int) (CNPJ.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso + 1;
                if (peso == 10) {
                    peso = 2;
                }
            }

            r = sm % 11;
            if ((r == 0) || (r == 1)) {
                dig14 = '0';
            } else {
                dig14 = (char) ((11 - r) + 48);
            }

// Verifica se os dígitos calculados conferem com os dígitos informados.
            if ((dig13 == CNPJ.charAt(12)) && (dig14 == CNPJ.charAt(13))) {
                return (true);
            } else {
                return (false);
            }
        } catch (InputMismatchException erro) {
            return (false);
        }
    }

    public static Pageable getPageRequest(UserQuery filter) {
        if (filter == null) {
            return null;
        }

        if (filter.getCurrentPage() == 0) {
            return null;
        }
        if (filter.getSortColumn() == null) {
            filter.setSortColumn("createAt");
        }
        if (filter.getSort() == null) {
            filter.setSort("asc");
        }

        String[] colunas = filter.getSortColumn().split(",");
        String[] ordens = filter.getSort().split(",");
        List<Sort.Order> lsOrdens = new ArrayList<>();
        for (int i = 0; i < colunas.length; i++) {
            lsOrdens.add(new Sort.Order(Sort.Direction.fromString(ordens.length - 1 < i ? "" : ordens[i].trim()), colunas[i].trim()));

        }
        return PageRequest.of(filter.getCurrentPage() > 0 ? filter.getCurrentPage() - 1 : 0, filter.getRowsPerPage(), Sort.by(lsOrdens));
    }
}
