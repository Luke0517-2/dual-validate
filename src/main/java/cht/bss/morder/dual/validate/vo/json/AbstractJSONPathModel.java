package cht.bss.morder.dual.validate.vo.json;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jayway.jsonpath.PathNotFoundException;
import com.jayway.jsonpath.ReadContext;

/**
 * @author 1909002 建立 json path model
 */
public abstract class AbstractJSONPathModel {

  /**
   * LOGGER
   */
  final static private Logger LOGGER = LoggerFactory.getLogger(AbstractJSONPathModel.class);

  /**
   * Json context
   */
  private final ReadContext ctx;

  /**
   * @param ctx ReadContext
   */
  public AbstractJSONPathModel(final ReadContext ctx) {
    this.ctx = ctx;
  }

  /**
   * 傳回依照jsonPath所取得的值
   * 
   * @deprecated 這個方法直接回傳Object不夠保險.
   *             <p>
   *             使用 {@link AbstractJSONPathModel#readValueAsString(String)} 取代.
   *
   * @param jsonPath jsonPath
   * @return 所取得的值
   */
  @Deprecated
  public Object read(final String jsonPath) {
    return ctx.read(jsonPath);
  }

  /**
   * 將jsonPath找到的值轉為String並回傳
   *
   * @param jsonPath jsonPath
   * @return 傳回依照jsonPath所取得的值 或 回傳null
   */
  public String readValueAsString(final String jsonPath) {
    String value = null;
    try {
      Object o = read(jsonPath);
      value = o != null ? Objects.toString(o) : null;
    } catch (PathNotFoundException e) {
      LOGGER.debug("jsonPath:{} in JSON:{}", jsonPath, jsonString());
    }

    return value;
  }

  /**
   * 原始的JSON 字串
   *
   * @return jsonString
   */
  public String jsonString() {
    return ctx.jsonString();
  }

  /**
   * 回傳ReadContext物件
   *
   * @return ReadContext物件
   */
  public ReadContext getReadContext() {
    return ctx;
  }

  /**
   * 用來取出JSON Path會回傳清單的結構
   *
   * @param pattern 字串樣式
   * @param args 樣式取代的參數
   * @return 數值清單
   */
  public Optional<List> getValuesByParams(final String pattern, final Object... args) {
    final String jsonPath = String.format(pattern, args);

    Optional<List> result;
    try {
      final List<Object> values = ctx.read(jsonPath);
      result = Optional.of(values);
    } catch (PathNotFoundException e) {
      LOGGER.debug("jsonPath:{} in JSON:{}", jsonPath, jsonString());
      result = Optional.empty();
    }
    return result;
  }

  /**
   * 用來取出JSON Path會回傳清單的結構
   *
   * @param pattern 字串樣式
   * @param args 樣式取代的參數
   * @return 數值清單
   */
  public Optional<List<?>> getValuesByParamsL(final String pattern, final Object... args) {
    final String jsonPath = String.format(pattern, args);
    Optional<List<?>> result;
    try {
      final List<Object> values = ctx.read(jsonPath);
      result = Optional.of(values);
    } catch (PathNotFoundException e) {
      LOGGER.debug("jsonPath:{} in JSON:{}", jsonPath, jsonString());
      result = Optional.empty();
    }
    return result;
  }

  public Optional<List<?>> getValues(final String jsonPath) {
    Optional<List<?>> result;
    try {
      final List<Object> values = ctx.read(jsonPath);
      result = Optional.of(values);
    } catch (PathNotFoundException e) {
      LOGGER.debug("jsonPath:{} in JSON:{}", jsonPath, jsonString());
      result = Optional.empty();
    }
    return result;
  }

  /**
   * 用來取出JSON Path會回傳單一值的結構
   *
   * @param pattern 字串樣式
   * @param args 樣式取代的參數
   * @return 單一值
   */
  public Optional<Object> getSingleValueByParams(final String pattern, final Object... args) {
    final String jsonPath = String.format(pattern, args);

    Optional<Object> result;
    try {
      final List<Object> values = ctx.read(jsonPath);
      result = values.isEmpty() ? Optional.empty() : Optional.of(values.get(0));
    } catch (PathNotFoundException e) {
      LOGGER.debug("jsonPath:{} in JSON:{}", jsonPath, jsonString());
      result = Optional.empty();
    }

    return result;
  }

  
  public abstract String getValue();
}
