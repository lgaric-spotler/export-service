package com.spotler.model.database;

import com.spotler.service.util.converter.DateTimeConverter;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.SQLDataType;

import java.time.LocalDateTime;
import java.util.List;

import static org.jooq.impl.DSL.*;

public class ExportTaskDb {
    private ExportTaskDb() {
        throw new IllegalStateException("Utility class");
    }

    public static final Table<Record> EXPORT_TASK_TABLE = table("ExportTask");

    public static final Field<Integer> ID_FIELD = field(name("id"), Integer.class);
    public static final Field<String> NAME_FIELD = field(name("name"), String.class);
    public static final Field<LocalDateTime> LAST_SUCCESSFUL_RUN_FIELD = field(name("lastSuccessfulRun"), SQLDataType.TIMESTAMP.asConvertedDataType(new DateTimeConverter()));

    public static final List<? extends Field<?>> FIELDS = List.of(ID_FIELD, NAME_FIELD, LAST_SUCCESSFUL_RUN_FIELD);
}
