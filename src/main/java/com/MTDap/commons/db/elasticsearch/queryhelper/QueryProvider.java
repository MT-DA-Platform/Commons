package com.MTDap.commons.db.elasticsearch.queryhelper;

import com.MTDap.commons.model.QueryParam;

public class QueryProvider implements IQueryProvider {
    private String name;

    private QueryParam queryParams;

    public QueryProvider(String name) {
        this(name, new QueryParam());
    }

    public QueryProvider(String name, QueryParam queryParams) {
        this.name = name;
        this.queryParams = queryParams;
    }

    @Override
    public String getNamedQuery() {
        return QueryLoader.getInstance().getNamedPreparedStatement(this.name, this.queryParams);
    }
}
