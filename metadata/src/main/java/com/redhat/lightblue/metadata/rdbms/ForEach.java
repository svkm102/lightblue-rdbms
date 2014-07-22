/*
 Copyright 2013 Red Hat, Inc. and/or its affiliates.

 This file is part of lightblue.

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.redhat.lightblue.metadata.rdbms;

import com.redhat.lightblue.metadata.parser.MetadataParser;
import com.redhat.lightblue.util.Path;

import java.util.List;

public class ForEach extends Expression {
    private Path iterateOverPath;
    private List<Expression> expressions;

    public void setIterateOverPath(Path iterateOverPath) {
        this.iterateOverPath = iterateOverPath;
    }

    public Path getIterateOverPath() {
        return iterateOverPath;
    }

    public void setExpressions(List<Expression> expressions) {
        this.expressions = expressions;
    }

    public List<Expression> getExpressions() {
        return expressions;
    }

    @Override
    public <T> void convert(MetadataParser<T> p, Object expressionsNode) {
        if (iterateOverPath == null || iterateOverPath.isEmpty()) {
            throw com.redhat.lightblue.util.Error.get(RDBMSConstants.ERR_FIELD_REQ, "No iterateOverPath informed");
        }
        if (expressions == null || expressions.size() == 0) {
            throw com.redhat.lightblue.util.Error.get(RDBMSConstants.ERR_FIELD_REQ, "No $foreach's expressions informed");
        }
        T eT = p.newNode();
        p.putString(eT, "iterateOverPath", iterateOverPath.toString());
        Object o = p.newArrayField(eT, "expressions");
        for (Expression expression : expressions) {
            expression.convert(p, o);
        }
        T s = p.newNode();
        p.putObject(s, "$foreach", eT);

        p.addObjectToArray(expressionsNode, s);;
    }
}
