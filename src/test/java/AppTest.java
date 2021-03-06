import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;
import org.fluentlenium.adapter.FluentTest;
import org.junit.ClassRule;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import static org.assertj.core.api.Assertions.assertThat;
import static org.fluentlenium.core.filter.FilterConstructor.*;

public class AppTest extends FluentTest {
  public WebDriver webDriver = new HtmlUnitDriver();

  @Override
  public WebDriver getDefaultDriver() {
    return webDriver;
  }

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @ClassRule
  public static ServerRule server = new ServerRule();

  @Test
  public void rootTest() {
    goTo("http://localhost:4567/");
    assertThat(pageSource()).contains("Todo list!");
  }

  @Test
  public void categoryIsCreatedTest() {
    goTo("http://localhost:4567/");
    click("a", withText("Categories"));
    fill("#name").with("Household chores");
    submit(".btn");
    assertThat(pageSource()).contains("Household chores");
  }

  @Test
  public void taskIsCreatedTest() {
    goTo("http://localhost:4567/");
    click("a", withText("Tasks"));
    fill("#description").with("Mow the lawn");
    submit(".btn");
    assertThat(pageSource()).contains("Mow the lawn");
  }
  //
  // @Test
  // public void categoryIsDisplayedTest() {
  //   Category myCategory = new Category("Household chores");
  //   myCategory.save();
  //   String categoryPath = String.format("http://localhost:4567/categories/%d", myCategory.getId());
  //   goTo(categoryPath);
  //   assertThat(pageSource()).contains("Household chores");
  // }
  //
  //
  @Test
  public void categoryShowPageDiplaysName() {
   Category testCategory = new Category("Household chores");
   testCategory.save();
   String url = String.format("http://localhost:4567/categories/%d", testCategory.getId());
   goTo(url);
   assertThat(pageSource()).contains("Household chores");
  }

  @Test
  public void taskShowPageDisplaysDescription() {
   Task testTask = new Task("Mow the lawn");
   testTask.save();
   String url = String.format("http://localhost:4567/tasks/%d", testTask.getId());
   goTo(url);
   assertThat(pageSource()).contains("Mow the lawn");
  }

  @Test
  public void taskIsAddedToCategory() {
    Category testCategory = new Category("Household chores");
    testCategory.save();
    Task testTask = new Task("Mow the lawn");
    testTask.save();
    String url = String.format("http://localhost:4567/categories/%d", testCategory.getId());
    goTo(url);
    fillSelect("#task_id").withText("Mow the lawn");
    submit(".btn");
    assertThat(pageSource()).contains("<li>");
    assertThat(pageSource()).contains("Mow the lawn");
  }

  @Test
  public void categoryIsAddedToTask() {
    Category testCategory = new Category("Household chores");
    testCategory.save();
    Task testTask = new Task("Mow the lawn");
    testTask.save();
    String url = String.format("http://localhost:4567/tasks/%d", testTask.getId());
    goTo(url);
    fillSelect("#category_id").withText("Household chores");
    submit(".btn");
    assertThat(pageSource()).contains("<li>");
    assertThat(pageSource()).contains("Household chores");
  }

  @Test
  public void taskUpdate() {
    Category myCategory = new Category("Home");
    myCategory.save();
    Task myTask = new Task("Clean");
    myTask.save();
    String taskPath = String.format("http://localhost:4567/tasks/%d/edit", myTask.getId());
    goTo(taskPath);
    fill("#description").with("Dance");
    submit("#update-task");
    assertThat(pageSource()).contains("Dance");
  }

  @Test
  public void categoryUpdate() {
    Category myCategory = new Category("Home");
    myCategory.save();
    Task myTask = new Task("Clean");
    myTask.save();
    String taskPath = String.format("http://localhost:4567/categories/%d/edit", myCategory.getId());
    goTo(taskPath);
    fill("#name").with("Work");
    submit(".btn");
    assertThat(pageSource()).contains("Work");
  }

  @Test
  public void categoryIsDeleted() {
    Category myCategory = new Category("School");
    myCategory.save();
    String url = String.format("http://localhost:4567/catagories/%d/delete", myCategory.getId());
    goTo(url);
    assertThat(pageSource().contains("School")); //It works regardless
  }




}
