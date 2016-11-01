package com.sample;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.squareup.sqldelight.RowMapper;
import com.squareup.sqldelight.SqlDelightStatement;
import java.lang.Deprecated;
import java.lang.Override;
import java.lang.String;
import java.util.Collections;

public interface TestModel {
  String TABLE_NAME = "test";

  String STUFF = "STUFF";

  String MYSTUFF = "mySTUFF";

  String LOWERCASE_STUFF = "lowercase_stuff";

  String MYOTHERSTUFF = "myOtherStuff";

  String CREATE_TABLE = ""
      + "CREATE TABLE test (\n"
      + "  STUFF TEXT NOT NULL,\n"
      + "  mySTUFF TEXT NOT NULL,\n"
      + "  lowercase_stuff TEXT NOT NULL,\n"
      + "  myOtherStuff TEXT NOT NULL\n"
      + ")";

  @NonNull
  String STUFF();

  @NonNull
  String mySTUFF();

  @NonNull
  String lowercase_stuff();

  @NonNull
  String myOtherStuff();

  interface Some_selectModel {
    @NonNull
    String mySTUFF();

    @NonNull
    String myOtherStuff();
  }

  interface Some_selectCreator<T extends Some_selectModel> {
    T create(@NonNull String mySTUFF, @NonNull String myOtherStuff);
  }

  final class Some_selectMapper<T extends Some_selectModel> implements RowMapper<T> {
    private final Some_selectCreator<T> creator;

    public Some_selectMapper(Some_selectCreator<T> creator) {
      this.creator = creator;
    }

    @Override
    @NonNull
    public T map(@NonNull Cursor cursor) {
      return creator.create(
          cursor.getString(0),
          cursor.getString(1)
      );
    }
  }

  interface Creator<T extends TestModel> {
    T create(@NonNull String STUFF, @NonNull String mySTUFF, @NonNull String lowercase_stuff, @NonNull String myOtherStuff);
  }

  final class Mapper<T extends TestModel> implements RowMapper<T> {
    private final Factory<T> testModelFactory;

    public Mapper(Factory<T> testModelFactory) {
      this.testModelFactory = testModelFactory;
    }

    @Override
    public T map(@NonNull Cursor cursor) {
      return testModelFactory.creator.create(
          cursor.getString(0),
          cursor.getString(1),
          cursor.getString(2),
          cursor.getString(3)
      );
    }
  }

  final class Marshal {
    protected final ContentValues contentValues = new ContentValues();

    Marshal(@Nullable TestModel copy) {
      if (copy != null) {
        this.STUFF(copy.STUFF());
        this.mySTUFF(copy.mySTUFF());
        this.lowercase_stuff(copy.lowercase_stuff());
        this.myOtherStuff(copy.myOtherStuff());
      }
    }

    public ContentValues asContentValues() {
      return contentValues;
    }

    public Marshal STUFF(String STUFF) {
      contentValues.put("STUFF", STUFF);
      return this;
    }

    public Marshal mySTUFF(String mySTUFF) {
      contentValues.put("mySTUFF", mySTUFF);
      return this;
    }

    public Marshal lowercase_stuff(String lowercase_stuff) {
      contentValues.put("lowercase_stuff", lowercase_stuff);
      return this;
    }

    public Marshal myOtherStuff(String myOtherStuff) {
      contentValues.put("myOtherStuff", myOtherStuff);
      return this;
    }
  }

  final class Factory<T extends TestModel> {
    public final Creator<T> creator;

    public Factory(Creator<T> creator) {
      this.creator = creator;
    }

    /**
     * @deprecated Use compiled statements (https://github.com/square/sqldelight#compiled-statements)
     */
    @Deprecated
    public Marshal marshal() {
      return new Marshal(null);
    }

    /**
     * @deprecated Use compiled statements (https://github.com/square/sqldelight#compiled-statements)
     */
    @Deprecated
    public Marshal marshal(TestModel copy) {
      return new Marshal(copy);
    }

    public SqlDelightStatement some_select() {
      return new SqlDelightStatement(""
          + "SELECT mySTUFF, myOtherStuff\n"
          + "FROM test",
      new String[0], Collections.<String>singleton("test"));
    }

    public <R extends Some_selectModel> Some_selectMapper<R> some_selectMapper(Some_selectCreator<R> creator) {
      return new Some_selectMapper<R>(creator);
    }
  }
}
