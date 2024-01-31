package com.paulorodrigues.authentication.commons.model;

import com.paulorodrigues.authentication.commons.util.PageableQuery;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.logging.log4j.util.Strings;

/**
 *
 * @author paulo.rodrigues
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserQuery extends PageableQuery {
    private String username;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("UserFilter{");
        if (Strings.isNotBlank(username)) {
            sb.append("username='").append(username).append('\'').append(", ");
        }
        return toStringSuper(sb).toString();
    }
}
