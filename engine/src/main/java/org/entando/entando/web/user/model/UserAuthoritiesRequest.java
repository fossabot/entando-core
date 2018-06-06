/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package org.entando.entando.web.user.model;

import java.util.ArrayList;
import java.util.stream.Collectors;
import org.entando.entando.web.user.annotation.GroupOrRoleNotBlank;

/**
 *
 * @author paddeo
 */
@GroupOrRoleNotBlank
public class UserAuthoritiesRequest extends ArrayList<UserAuthority> {

    @Override
    public String toString() {
        return "UserAuthoritiesRequest: [" + this.stream().map(e -> e.toString()).collect(Collectors.joining(",")) + "]";
    }

}
