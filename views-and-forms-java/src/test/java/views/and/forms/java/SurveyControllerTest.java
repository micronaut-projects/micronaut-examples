package views.and.forms.java;

import io.micronaut.test.annotation.MicronautTest;
import io.micronaut.views.ModelAndView;
import org.junit.jupiter.api.Test;
import views.and.forms.java.controllers.SurveyController;
import views.and.forms.java.model.FormData;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
public class SurveyControllerTest {

    @Inject
    SurveyController itemUnderTest;

//    @Test
//    void testHome_View() {
//
//        ModelAndView actual = itemUnderTest.home();
//
//        assertEquals(actual.getView().get(), "home");
//    }
//
//    @Test
//    void testHome_FruitChoices() {
//
//        FormData fakeFormData =
//                new FormData(new String[] {"banana", "mango", "apple", "orange", "grapes", "star"});
//
//        ModelAndView actualModelAndView = itemUnderTest.home();
//
//        FormData actualFormData = (FormData) actualModelAndView.getModel().get();
//
//        assertArrayEquals(actualFormData.getFruitChoices(), fakeFormData.getFruitChoices());
//    }
//
//    @Test
//    void testProcessHomeScreen_userName() {
//
//        FormData fakeFormData = new FormData("fakeUser", "", "like",  null, null, "");
//
//        ModelAndView actualModelAndView = itemUnderTest.processHomeScreen(fakeFormData);
//        FormData actualFormData = (FormData) actualModelAndView.getModel().get();
//
//        assertEquals(actualFormData.getUserName(), "fakeUser");
//
//    }
//
//    @Test
//    void testThankYou_invalidUserName() {
//
//        FormData fakeFormData = new FormData("u", "", "like",  null, null, "");
//
//        ModelAndView actualModelAndView = itemUnderTest.processHomeScreen(fakeFormData);
//        FormData actualFormData = (FormData) actualModelAndView.getModel().get();
//
//        assertEquals(actualFormData.getUserName(), fakeFormData.getUserName());
//        assertEquals(actualModelAndView.getView().get(), "home");
//
//    }
//
//    @Test
//    void testThankYou_chocolate() {
//
//        FormData fakeFormData = new FormData("fakeUser", "", "like",  null, null, "");
//
//        ModelAndView actualModelAndView = itemUnderTest.processHomeScreen(fakeFormData);
//        FormData actualFormData = (FormData) actualModelAndView.getModel().get();
//
//        assertEquals(actualFormData.getChocolate(), "like");
//    }
//
//    @Test
//    void testThankYou_banana() {
//
//        String[] fakeFruitArray = new String[]{"banana"};
//        FormData fakeFormData = new FormData("fakeUser", "", "like", null, fakeFruitArray, "");
//
//        ModelAndView actualModelAndView = itemUnderTest.processHomeScreen(fakeFormData);
//        FormData actualFormData = (FormData) actualModelAndView.getModel().get();
//
//        assertArrayEquals(actualFormData.getFruitChoices(), fakeFruitArray);
//    }
//
//    @Test
//    void testThankYou_chosenFruit() {
//
//        String[] fakeFruitArray = new String[]{"banana", "mango", "star"};
//        FormData fakeFormData = new FormData("fakeUser", "", "like", null, fakeFruitArray, "");
//
//        ModelAndView actualModelAndView = itemUnderTest.processHomeScreen(fakeFormData);
//        FormData actualFormData = (FormData) actualModelAndView.getModel().get();
//
//        List<String> fakeFruitList = new ArrayList<String>(
//                Arrays.asList("banana", "mango", "star"));
//
//        assertEquals(actualFormData.getFruitChoices().length, fakeFruitArray.length);
//        assertArrayEquals(actualFormData.getFruitChoices(), fakeFruitArray);
//
//    }
//
//    @Test
//    void testThankYou_noChosenFruit() {
//        String[] fakeFruitArray = new String[]{};
//        FormData fakeFormData = new FormData("fakeUser", "", "like", null, fakeFruitArray, "");
//
//        ModelAndView actualModelAndView = itemUnderTest.processHomeScreen(fakeFormData);
//        FormData actualFormData = (FormData) actualModelAndView.getModel().get();
//
//        assertEquals(actualFormData.getFruitChoices().length, 0);
//    }

}
