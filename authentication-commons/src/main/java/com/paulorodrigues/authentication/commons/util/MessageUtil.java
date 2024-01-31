/*
 * Copyright (C) 2021 paulo.rodrigues
 * Profile: <https://github.com/mrpaulo>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.paulorodrigues.authentication.commons.util;

import org.springframework.beans.factory.annotation.Value;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author paulo.rodrigues
 */
public class MessageUtil {

    @Value("${app.language.default}")
    private static String appLanguage;
    private static final Locale locale = new Locale("pt");
    private static final ResourceBundle messagesRB = ResourceBundle.getBundle("resourcebundle.messages", locale);
    private static final String ERROR_MESSAGE_NOT_FOUND = "Error message not found";

    public static String getMessage(String... args) {

        if (args.length == 1) {

            return getRB(args[0]);

        } else if (args.length > 1) {

            List<String> params = new ArrayList<>();
            for (int i = 1; i < args.length; i++) {
                params.add(args[i]);
            }

            String[] newParams = params.stream().toArray(String[]::new);

            return MessageFormat.format(getRB(args[0]), newParams);
        }

        return ERROR_MESSAGE_NOT_FOUND;
    }

    public static String getRB(String message) {
        if (messagesRB.containsKey(message)) {
            return messagesRB.getString(message);
        }
        return ERROR_MESSAGE_NOT_FOUND;
    }

}
