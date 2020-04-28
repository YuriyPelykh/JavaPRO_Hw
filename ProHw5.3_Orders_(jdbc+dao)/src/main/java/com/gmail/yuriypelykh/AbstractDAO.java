package com.gmail.yuriypelykh;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDAO<K, T> {
    private final Connection conn;
    private final String table;

    public AbstractDAO(Connection conn, String table) {
        this.conn = conn;
        this.table = table;
    }

    public void add(T t) {
        try {
            Field[] fields = t.getClass().getDeclaredFields();

            StringBuilder names = new StringBuilder();
            StringBuilder values = new StringBuilder();
            StringBuilder namesValues = new StringBuilder();

            for (Field f : fields) {
                f.setAccessible(true);

                names.append(f.getName()).append(',');
                values.append('"').append(f.get(t)).append("\",");
                if (f.getName().equals("id")) {
                    continue;
                }
                namesValues.append(f.getName()).append(" = \'").append(f.get(t)).append("\' and ");
            }
            names.deleteCharAt(names.length() - 1); // last ','
            values.deleteCharAt(values.length() - 1); // last ','
            namesValues.delete(namesValues.length() - 4, namesValues.length()); // last "and "

            String sql = "INSERT INTO " + table + "(" + names.toString() + ") VALUES(" + values.toString() + ")";
            try (Statement st = conn.createStatement()) {
                st.execute(sql);
            }
            try {
                try (Statement st = conn.createStatement()) {
                    try (ResultSet rs = st.executeQuery("SELECT id FROM " + table + " WHERE " + namesValues.toString())) {
                        if (rs.next()) {
                            for (Field f : fields) {
                                if (f.getName().equals("id")) {
                                    f.set(t, rs.getInt(1));
                                }
                            }
                        } else {
                            throw new RuntimeException("Return ID failed");
                        }
                    }
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void update(T t) {
        try {
            Field[] fields = t.getClass().getDeclaredFields();
            Field id = null;

            for (Field f : fields) {
                if (f.isAnnotationPresent(Id.class)) {
                    id = f;
                    id.setAccessible(true);
                    break;
                }
            }
            if (id == null)
                throw new RuntimeException("No Id field");

            StringBuilder sb = new StringBuilder();

            for (Field f : fields) {
                if (f != id) {
                    f.setAccessible(true);

                    sb.append(f.getName())
                            .append(" = ")
                            .append('"')
                            .append(f.get(t))
                            .append('"')
                            .append(',');
                }
            }

            sb.deleteCharAt(sb.length() - 1);

            String sql = "UPDATE " + table + " SET " + sb.toString() + " WHERE " +
                    id.getName() + " = \''" + id.get(t) + "\''";

            try (Statement st = conn.createStatement()) {
                st.execute(sql);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void delete(T t) {
        try {
            Field[] fields = t.getClass().getDeclaredFields();
            Field id = null;

            for (Field f : fields) {
                if (f.isAnnotationPresent(Id.class)) {
                    id = f;
                    id.setAccessible(true);
                    break;
                }
            }
            if (id == null)
                throw new RuntimeException("No Id field");

            String sql = "DELETE FROM " + table + " WHERE " + id.getName() +
                    " = \''" + id.get(t) + "\''";

            try (Statement st = conn.createStatement()) {
                st.execute(sql);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public List<T> getAll(Class<T> cls) {
        List<T> res = new ArrayList<>();

        try {
            try (Statement st = conn.createStatement()) {
                try (ResultSet rs = st.executeQuery("SELECT * FROM " + table)) {
                    ResultSetMetaData md = rs.getMetaData();

                    while (rs.next()) {
                        T t = cls.newInstance();

                        for (int i = 1; i <= md.getColumnCount(); i++) {
                            String columnName = md.getColumnName(i);

                            Field field = cls.getDeclaredField(columnName);
                            field.setAccessible(true);

                            field.set(t, rs.getObject(columnName));
                        }

                        res.add(t);
                    }
                }
            }

            return res;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public List<T> getWhere(Class<T> cls, String paramName, String operation, K value) {
        List<T> res = new ArrayList<>();

        try {
            try (Statement st = conn.createStatement()) {
                try (ResultSet rs = st.executeQuery("SELECT * FROM " + table + " WHERE " + paramName +
                        " " + operation + " \'" + value + "\'")) {
                    ResultSetMetaData md = rs.getMetaData();

                    while (rs.next()) {
                        T t = cls.newInstance();

                        for (int i = 1; i <= md.getColumnCount(); i++) {
                            String columnName = md.getColumnName(i);

                            Field field = cls.getDeclaredField(columnName);
                            field.setAccessible(true);

                            field.set(t, rs.getObject(columnName));
                        }

                        res.add(t);
                    }
                }
            }

            return res;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public long count() {
        try {
            try (Statement st = conn.createStatement()) {
                try (ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM " + table)) {
                    if (rs.next())
                        return rs.getLong(1);
                    else
                        throw new RuntimeException("Count failed");
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public int getId(T t) {
        Field fid = null;
        int id = 0;
        try {
            Field[] fields = t.getClass().getDeclaredFields();

            for (Field f : fields) {
                if (f.isAnnotationPresent(Id.class)) {
                    fid = f;
                    fid.setAccessible(true);
                    id = (int) fid.get(t);
                    break;
                }
            }
            if (fid == null)
                throw new RuntimeException("No Id field");
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return id;
    }
}
