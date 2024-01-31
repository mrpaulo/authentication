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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import static com.paulorodrigues.authentication.commons.util.FormatUtil.removeLastComma;


/**
 *
 * @author paulo.rodrigues
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PageableQuery {
    
    private int currentPage;
    private int rowsPerPage;
    private String sortColumn;
    private String sort;
    private int offset;
    
    private Long id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;

    private LocalDate createAt;
    private String createBy;
    private LocalDate updateAt;
    private String updateBy;
    public StringBuilder toStringSuper(StringBuilder sb){
        sb.append("currentPage=").append(getCurrentPage()).append(", ");
        sb.append("rowsPerPage=").append(getRowsPerPage()).append(", ");
        if (getSortColumn() != null && !getSortColumn().isEmpty()) {
            sb.append("sortColumn='").append(getSortColumn()).append('\'').append(", ");
        }
        if (getSort() != null && !getSort().isEmpty()) {
            sb.append("sort='").append(getSort()).append('\'').append(", ");
        }
        sb.append("offset=").append(getOffset()).append(", ");
        if (getId() != null) {
            sb.append("id=").append(getId()).append(", ");
        }
        if (getName() != null && !getName().isEmpty()) {
            sb.append("name='").append(getName()).append('\'').append(", ");
        }
        if (getStartDate() != null) {
            sb.append("startDate=").append(getStartDate()).append(", ");
        }
        if (getEndDate() != null) {
            sb.append("finalDate=").append(getEndDate()).append(", ");
        }
        sb = removeLastComma(sb);
        sb.append('}');
        return sb;
    }
}
