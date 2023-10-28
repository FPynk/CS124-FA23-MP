package edu.illinois.cs.cs124.ay2023.mp.test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSubstring;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.google.common.truth.Truth.assertWithMessage;
import static edu.illinois.cs.cs124.ay2023.mp.test.helpers.Data.SUMMARIES;
import static edu.illinois.cs.cs124.ay2023.mp.test.helpers.Data.SUMMARY_COUNT;
import static edu.illinois.cs.cs124.ay2023.mp.test.helpers.Helpers.pause;
import static edu.illinois.cs.cs124.ay2023.mp.test.helpers.Helpers.startMainActivity;
import static edu.illinois.cs.cs124.ay2023.mp.test.helpers.RecyclerViewMatcher.withRecyclerView;
import static edu.illinois.cs.cs124.ay2023.mp.test.helpers.Views.countRecyclerView;
import static edu.illinois.cs.cs124.ay2023.mp.test.helpers.Views.searchFor;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import edu.illinois.cs.cs124.ay2023.mp.R;
import edu.illinois.cs.cs124.ay2023.mp.models.Summary;
import edu.illinois.cs.cs125.gradlegrader.annotations.Graded;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.robolectric.annotation.LooperMode;

/*
 * This is the MP1 test suite.
 * The code below is used to evaluate your app during testing, local grading, and official grading.
 * You may not understand all of the code below, but you'll need to have some understanding of how
 * it works so that you can determine what is wrong with your app and what you need to fix.
 *
 * ALL CHANGES TO THIS FILE WILL BE OVERWRITTEN DURING OFFICIAL GRADING.
 * You can and should modify the code below if it is useful during your own local testing,
 * but any changes you make will be discarded during official grading.
 * The local grader will not run if the test suites have been modified, so you'll need to undo any
 * local changes before you run the grader.
 *
 * Note that this means that you should not fix problems with the app by modifying the test suites.
 * The test suites are always considered to be correct.
 *
 * Our test suites are broken into two parts.
 * The unit tests (in the UnitTests class) are tests that we can perform without running your app.
 * They test things like whether a method works properly or the behavior of your API server.
 * Unit tests are usually fairly fast.
 *
 * The integration tests (in the IntegrationTests class) are tests that require simulating your app.
 * This allows us to test things like your API client, and higher-level aspects of your app's
 * behavior, such as whether it displays the right thing on the display.
 * Because integration tests require simulating your app, they run more slowly.
 *
 * The MP1 test suite includes no ungraded tests.
 * Note that test0_SummaryComparison and test1_SummaryFilter were generated from the MP reference
 * solution, and as such do not represent what a real-world test suite would typically look like.
 * (It would have fewer examples chosen more carefully.)
 */

@RunWith(Enclosed.class)
public final class MP1Test {
  // Unit tests that don't require simulating the entire app, and usually complete quickly
  @FixMethodOrder(MethodSorters.NAME_ASCENDING)
  public static class UnitTests {
    // Private copy of the summaries list, shuffled to improve testing
    private static final List<Summary> SHUFFLED_SUMMARIES;

    // Set up our shuffled list
    static {
      List<Summary> shuffledSummaries = new ArrayList<>(SUMMARIES);
      // Seed the random number generator for reproducibility
      Collections.shuffle(shuffledSummaries, new Random(124));
      // Make the list immutable to detect modifications during testing
      SHUFFLED_SUMMARIES = Collections.unmodifiableList(shuffledSummaries);
    }

    // Helper method for test0_SummaryComparison
    private void summaryComparatorHelper(int firstIndex, int secondIndex, int expected) {
      // first and second should be indices into our shuffled list of summaries
      Summary first = SHUFFLED_SUMMARIES.get(firstIndex);
      assertWithMessage("Invalid summary index: " + firstIndex).that(first).isNotNull();
      Summary second = SHUFFLED_SUMMARIES.get(secondIndex);
      assertWithMessage("Invalid summary index: " + secondIndex).that(second).isNotNull();

      // Test the forward comparison
      int forward = first.compareTo(second);
      if (expected == 0) {
        assertWithMessage("Summaries " + first + " and " + second + " should be compared equal")
            .that(forward)
            .isEqualTo(0);
      } else if (expected < 0) {
        assertWithMessage("Summary " + first + " should be less than " + second)
            .that(forward)
            .isLessThan(0);
      } else {
        assertWithMessage("Summary " + first + " should be greater than " + second)
            .that(forward)
            .isGreaterThan(0);
      }

      // Test the reverse comparison
      int reverse = second.compareTo(first);
      if (expected == 0) {
        assertWithMessage("Summaries " + second + " and " + first + " should be compared equal")
            .that(reverse)
            .isEqualTo(0);
      } else if (expected < 0) {
        assertWithMessage("Summary " + second + " should be less than " + first)
            .that(reverse)
            .isGreaterThan(0);
      } else {
        assertWithMessage("Summary " + second + " should be greater than " + first)
            .that(reverse)
            .isLessThan(0);
      }
    }

    /** Test the summary default comparison (compareTo). */
    @Test(timeout = 1000L)
    @Graded(points = 25, friendlyName = "Summary Comparison")
    public void test0_SummaryComparison() {
      // Test a variety of pairs chosen randomly from our shuffled list of summaries
      // Test self-comparisons
      summaryComparatorHelper(200, 200, 0);
      summaryComparatorHelper(10, 10, 0);
      summaryComparatorHelper(178, 178, 0);
      summaryComparatorHelper(92, 92, 0);
      // Test random pairs
      summaryComparatorHelper(52, 118, 16);
      summaryComparatorHelper(208, 193, -2);
      summaryComparatorHelper(284, 88, -2);
      summaryComparatorHelper(120, 221, -2);
      summaryComparatorHelper(248, 262, -6);
      summaryComparatorHelper(248, 101, 4);
      summaryComparatorHelper(270, 2, -3);
      summaryComparatorHelper(57, 253, -3);
      summaryComparatorHelper(105, 17, 1);
      summaryComparatorHelper(70, 287, -2);
      summaryComparatorHelper(278, 64, 2);
      summaryComparatorHelper(272, 81, -6);
      summaryComparatorHelper(288, 255, -2);
      summaryComparatorHelper(73, 36, 2);
      summaryComparatorHelper(266, 76, 6);
      summaryComparatorHelper(63, 283, 1);
      summaryComparatorHelper(23, 121, -6);
      summaryComparatorHelper(63, 63, 0);
      summaryComparatorHelper(200, 110, -2);
      summaryComparatorHelper(78, 255, -1);
      summaryComparatorHelper(12, 182, -1);
      summaryComparatorHelper(79, 111, -16);
      summaryComparatorHelper(46, 262, -3);
      summaryComparatorHelper(200, 89, -3);
      summaryComparatorHelper(35, 119, -6);
      summaryComparatorHelper(46, 97, -2);
      summaryComparatorHelper(184, 140, -2);
      summaryComparatorHelper(145, 241, -3);
      summaryComparatorHelper(176, 299, 2);
      summaryComparatorHelper(263, 94, 5);
      summaryComparatorHelper(103, 161, 4);
      summaryComparatorHelper(166, 128, -6);
      summaryComparatorHelper(171, 154, -16);
      summaryComparatorHelper(115, 6, -1);
      summaryComparatorHelper(3, 67, 6);
      summaryComparatorHelper(165, 10, -1);
      summaryComparatorHelper(245, 210, -1);
      summaryComparatorHelper(257, 269, -1);
      summaryComparatorHelper(51, 300, 1);
      summaryComparatorHelper(28, 263, 1);
      summaryComparatorHelper(142, 141, -3);
      summaryComparatorHelper(32, 184, -1);
      summaryComparatorHelper(33, 91, -3);
      summaryComparatorHelper(201, 292, 2);
      summaryComparatorHelper(18, 79, 1);
      summaryComparatorHelper(162, 38, -16);
      summaryComparatorHelper(28, 217, 2);
      summaryComparatorHelper(202, 55, 1);
      summaryComparatorHelper(63, 0, -1);
      summaryComparatorHelper(241, 43, -6);
      summaryComparatorHelper(100, 171, -4);
      summaryComparatorHelper(116, 95, 16);
      summaryComparatorHelper(98, 72, -3);
      summaryComparatorHelper(98, 179, -2);
      summaryComparatorHelper(108, 122, 6);
      summaryComparatorHelper(299, 5, -2);
      summaryComparatorHelper(66, 188, -6);
      summaryComparatorHelper(184, 262, -1);
      summaryComparatorHelper(153, 52, -2);
      summaryComparatorHelper(300, 32, 6);
      summaryComparatorHelper(80, 70, 2);
      summaryComparatorHelper(184, 238, -1);
      summaryComparatorHelper(194, 9, 6);
      summaryComparatorHelper(124, 77, -3);
    }

    // Helper method to convert a list of summaries into a list of indices into our
    // shuffled list of summaries
    private List<Integer> summaryListToPositionList(List<Summary> list) {
      return list.stream().map(SHUFFLED_SUMMARIES::indexOf).collect(Collectors.toList());
    }

    // Helper method for test1_SummaryFilter
    private void summaryFilterHelper(
        List<Summary> list, String filter, int size, List<Integer> expectedPositions) {
      // Filter the list using the summary filter
      List<Summary> filteredList = Summary.filter(list, filter);
      // Filtered list should never be null
      assertWithMessage("List filtered with \"" + filter + "\" should not be null")
          .that(filteredList)
          .isNotNull();
      // Filtered list should return a new list
      assertWithMessage("List filter should return a new list")
          .that(filteredList)
          .isNotSameInstanceAs(list);
      // Check the size of the filtered list
      assertWithMessage("List filtered with \"" + filter + "\" should have size " + size)
          .that(filteredList)
          .hasSize(size);
      // Check whether the filtered list includes the right summaries in the correct positions
      if (expectedPositions != null) {
        List<Integer> positions = summaryListToPositionList(filteredList);
        assertWithMessage("List positions filtered with \"" + filter + "\" is not the right size")
            .that(positions)
            .hasSize(expectedPositions.size());
        for (int i = 0; i < positions.size(); i++) {
          assertWithMessage("Summary in incorrect position using filter \"" + filter + "\"")
              .that(positions.get(i))
              .isEqualTo(expectedPositions.get(i));
        }
      }
    }

    /**
     * Test summary filtering.
     *
     * @noinspection SpellCheckingInspection, RedundantSuppression
     */
    @Test(timeout = 1000L)
    @Graded(points = 25, friendlyName = "Summary Filtering")
    public void test1_SummaryFilter() {
      // Test a variety of filtering calls, most on our shuffled list of summaries
      // Test a few searches on the empty list
      summaryFilterHelper(Collections.emptyList(), "", 0, Collections.emptyList());
      summaryFilterHelper(Collections.emptyList(), " ", 0, Collections.emptyList());
      summaryFilterHelper(Collections.emptyList(), " test ", 0, Collections.emptyList());

      // Test a variety of searches on the shuffled list of summaries
      // Generate tests from reference solution
      summaryFilterHelper(SHUFFLED_SUMMARIES, "oast", 0, List.of());
      summaryFilterHelper(SHUFFLED_SUMMARIES, "ose", 1, List.of(105));
      summaryFilterHelper(SHUFFLED_SUMMARIES, "brom", 0, List.of());
      summaryFilterHelper(SHUFFLED_SUMMARIES, "data", 36, null);
      summaryFilterHelper(SHUFFLED_SUMMARIES, "and ", 41, null);
      summaryFilterHelper(SHUFFLED_SUMMARIES, "advanced", 36, null);
      summaryFilterHelper(SHUFFLED_SUMMARIES, " 571 ", 3, Arrays.asList(274, 208, 301));
      summaryFilterHelper(SHUFFLED_SUMMARIES, " bits ", 1, List.of(124));
      summaryFilterHelper(SHUFFLED_SUMMARIES, "youth", 5, Arrays.asList(204, 179, 106, 16, 147));
      summaryFilterHelper(SHUFFLED_SUMMARIES, "research", 14, null);
      summaryFilterHelper(
          SHUFFLED_SUMMARIES, "professional", 6, Arrays.asList(113, 210, 273, 102, 169, 230));
      summaryFilterHelper(SHUFFLED_SUMMARIES, "race,", 1, List.of(70));
      summaryFilterHelper(SHUFFLED_SUMMARIES, "evaluation", 1, List.of(57));
      summaryFilterHelper(SHUFFLED_SUMMARIES, "430", 2, Arrays.asList(233, 260));
      summaryFilterHelper(SHUFFLED_SUMMARIES, "425 ", 2, Arrays.asList(286, 180));
      summaryFilterHelper(SHUFFLED_SUMMARIES, " 446 ", 2, Arrays.asList(290, 149));
      summaryFilterHelper(SHUFFLED_SUMMARIES, "310", 1, List.of(115));
      summaryFilterHelper(SHUFFLED_SUMMARIES, "librakianship", 0, List.of());
      summaryFilterHelper(SHUFFLED_SUMMARIES, "probavility", 0, List.of());
      summaryFilterHelper(SHUFFLED_SUMMARIES, "covputer", 0, List.of());
      summaryFilterHelper(SHUFFLED_SUMMARIES, "text", 2, Arrays.asList(68, 104));
      summaryFilterHelper(SHUFFLED_SUMMARIES, "communication", 1, List.of(176));
      summaryFilterHelper(SHUFFLED_SUMMARIES, "programming", 12, null);
      summaryFilterHelper(SHUFFLED_SUMMARIES, "5k5", 0, List.of());
      summaryFilterHelper(SHUFFLED_SUMMARIES, "dath", 0, List.of());
      summaryFilterHelper(SHUFFLED_SUMMARIES, "science ", 30, null);
      summaryFilterHelper(SHUFFLED_SUMMARIES, "in", 178, null);
      summaryFilterHelper(SHUFFLED_SUMMARIES, "dawa", 0, List.of());
      summaryFilterHelper(SHUFFLED_SUMMARIES, "wodeling", 0, List.of());
      summaryFilterHelper(SHUFFLED_SUMMARIES, "c05", 0, List.of());
      summaryFilterHelper(SHUFFLED_SUMMARIES, "is", 177, null);
      summaryFilterHelper(SHUFFLED_SUMMARIES, "analysis", 14, null);
      summaryFilterHelper(SHUFFLED_SUMMARIES, "iv", 13, null);
      summaryFilterHelper(SHUFFLED_SUMMARIES, "laboratory", 2, Arrays.asList(228, 45));
      summaryFilterHelper(SHUFFLED_SUMMARIES, "569", 1, List.of(48));
      summaryFilterHelper(SHUFFLED_SUMMARIES, "deduction ", 1, List.of(173));
      summaryFilterHelper(SHUFFLED_SUMMARIES, "y45", 0, List.of());
      summaryFilterHelper(SHUFFLED_SUMMARIES, "cutrent", 0, List.of());
      summaryFilterHelper(
          SHUFFLED_SUMMARIES, "theory", 8, Arrays.asList(43, 99, 91, 47, 275, 232, 143, 289));
      summaryFilterHelper(SHUFFLED_SUMMARIES, "problems", 3, Arrays.asList(200, 53, 4));
      summaryFilterHelper(
          SHUFFLED_SUMMARIES, "fundamentals", 5, Arrays.asList(152, 54, 67, 199, 189));
      summaryFilterHelper(
          SHUFFLED_SUMMARIES, "rs", 7, Arrays.asList(239, 55, 87, 172, 208, 181, 73));
      summaryFilterHelper(SHUFFLED_SUMMARIES, "access", 3, Arrays.asList(251, 46, 63));
      summaryFilterHelper(SHUFFLED_SUMMARIES, "stat ", 59, null);
      summaryFilterHelper(SHUFFLED_SUMMARIES, "stochastip", 0, List.of());
      summaryFilterHelper(SHUFFLED_SUMMARIES, "59l", 0, List.of());
      summaryFilterHelper(SHUFFLED_SUMMARIES, " is", 177, null);
      summaryFilterHelper(SHUFFLED_SUMMARIES, "491", 2, Arrays.asList(61, 291));
      summaryFilterHelper(
          SHUFFLED_SUMMARIES, "network", 7, Arrays.asList(132, 237, 158, 66, 176, 3, 10));
      summaryFilterHelper(SHUFFLED_SUMMARIES, "571", 3, Arrays.asList(274, 208, 301));
      summaryFilterHelper(SHUFFLED_SUMMARIES, "496", 1, List.of(304));
      summaryFilterHelper(SHUFFLED_SUMMARIES, "bs", 2, Arrays.asList(261, 145));
      summaryFilterHelper(SHUFFLED_SUMMARIES, "ischool", 2, Arrays.asList(11, 119));
      summaryFilterHelper(SHUFFLED_SUMMARIES, "530", 2, Arrays.asList(65, 163));
      summaryFilterHelper(SHUFFLED_SUMMARIES, "computer", 21, null);
      summaryFilterHelper(SHUFFLED_SUMMARIES, "455", 1, List.of(265));
      summaryFilterHelper(SHUFFLED_SUMMARIES, "499", 1, List.of(203));
      summaryFilterHelper(SHUFFLED_SUMMARIES, "59x", 0, List.of());
      summaryFilterHelper(SHUFFLED_SUMMARIES, "developmwnt", 0, List.of());
      summaryFilterHelper(SHUFFLED_SUMMARIES, "443 ", 2, Arrays.asList(17, 210));
      summaryFilterHelper(SHUFFLED_SUMMARIES, "51q", 0, List.of());
      summaryFilterHelper(SHUFFLED_SUMMARIES, "and", 41, null);
      summaryFilterHelper(SHUFFLED_SUMMARIES, "43u", 0, List.of());
      summaryFilterHelper(SHUFFLED_SUMMARIES, "of", 41, null);
      summaryFilterHelper(SHUFFLED_SUMMARIES, "i ", 295, null);
      summaryFilterHelper(SHUFFLED_SUMMARIES, "roce,", 0, List.of());
      summaryFilterHelper(SHUFFLED_SUMMARIES, " human-centered ", 3, Arrays.asList(304, 193, 11));
      summaryFilterHelper(SHUFFLED_SUMMARIES, "418", 2, Arrays.asList(224, 155));
      summaryFilterHelper(SHUFFLED_SUMMARIES, "introduction", 15, null);
      summaryFilterHelper(SHUFFLED_SUMMARIES, "stat", 59, null);
      summaryFilterHelper(SHUFFLED_SUMMARIES, "orientation", 1, List.of(164));
      summaryFilterHelper(SHUFFLED_SUMMARIES, "56d", 0, List.of());
      summaryFilterHelper(SHUFFLED_SUMMARIES, "tte", 0, List.of());
      summaryFilterHelper(
          SHUFFLED_SUMMARIES, "social", 7, Arrays.asList(101, 33, 158, 32, 256, 296, 89));
      summaryFilterHelper(SHUFFLED_SUMMARIES, "topics", 39, null);
      summaryFilterHelper(SHUFFLED_SUMMARIES, "ci", 49, null);
      summaryFilterHelper(SHUFFLED_SUMMARIES, "ou", 15, null);
      summaryFilterHelper(SHUFFLED_SUMMARIES, "yomputer", 0, List.of());
      summaryFilterHelper(SHUFFLED_SUMMARIES, "i", 295, null);
      summaryFilterHelper(SHUFFLED_SUMMARIES, "cn", 0, List.of());
      summaryFilterHelper(SHUFFLED_SUMMARIES, "accelerated", 4, Arrays.asList(54, 67, 199, 189));
      summaryFilterHelper(SHUFFLED_SUMMARIES, "dyta", 0, List.of());
      summaryFilterHelper(SHUFFLED_SUMMARIES, "581", 2, Arrays.asList(246, 179));
      summaryFilterHelper(SHUFFLED_SUMMARIES, "461", 2, Arrays.asList(109, 269));
      summaryFilterHelper(SHUFFLED_SUMMARIES, "cs", 169, null);
      summaryFilterHelper(SHUFFLED_SUMMARIES, "the", 22, null);
      summaryFilterHelper(SHUFFLED_SUMMARIES, "with", 1, List.of(11));
      summaryFilterHelper(SHUFFLED_SUMMARIES, "sociotechnical", 1, List.of(222));
      summaryFilterHelper(SHUFFLED_SUMMARIES, "machine", 9, null);
      summaryFilterHelper(SHUFFLED_SUMMARIES, "lpnguage", 0, List.of());
      summaryFilterHelper(SHUFFLED_SUMMARIES, "eopics", 0, List.of());
      summaryFilterHelper(SHUFFLED_SUMMARIES, "information", 39, null);
      summaryFilterHelper(SHUFFLED_SUMMARIES, "to", 76, null);
      summaryFilterHelper(SHUFFLED_SUMMARIES, "time", 3, Arrays.asList(226, 118, 174));
    }
  }

  // Integration tests that require simulating the entire app, and are usually slower
  @RunWith(AndroidJUnit4.class)
  @LooperMode(LooperMode.Mode.PAUSED)
  @FixMethodOrder(MethodSorters.NAME_ASCENDING)
  public static class IntegrationTests {
    /** Test summary view to make sure that the correct courses are displayed in the right order. */
    @Test(timeout = 10000L)
    @Graded(points = 20, friendlyName = "Summary View")
    public void test2_SummaryView() {
      startMainActivity(
          activity -> {
            // Check that the right number of summaries are displayed
            onView(ViewMatchers.withId(R.id.recycler_view)).check(countRecyclerView(SUMMARY_COUNT));

            // Check that full summary titles are shown, and that the the order is correct
            onView(withRecyclerView(R.id.recycler_view).atPosition(0))
                .check(matches(hasDescendant(withText("CS 100: Computer Science Orientation"))));
            onView(withRecyclerView(R.id.recycler_view).atPosition(1))
                .check(matches(hasDescendant(withSubstring("IS 100"))));
            onView(withRecyclerView(R.id.recycler_view).atPosition(2))
                .check(matches(hasDescendant(withSubstring("STAT 100"))));

            // Check a pair that won't sort properly just based on number
            onView(withId(R.id.recycler_view)).perform(scrollToPosition(73));
            onView(withRecyclerView(R.id.recycler_view).atPosition(73))
                .check(matches(hasDescendant(withSubstring("CS 403"))));
            onView(withId(R.id.recycler_view)).perform(scrollToPosition(74));
            onView(withRecyclerView(R.id.recycler_view).atPosition(74))
                .check(matches(hasDescendant(withSubstring("IS 403"))));

            // Check the endpoint
            onView(withId(R.id.recycler_view)).perform(scrollToPosition(SUMMARY_COUNT - 1));
            onView(withRecyclerView(R.id.recycler_view).atPosition(SUMMARY_COUNT - 1))
                .check(matches(hasDescendant(withText("STAT 599: Thesis Research"))));
          });
    }

    /**
     * Test search interaction to make sure that the correct courses are shown when the search
     * feature is used.
     */
    @Test(timeout = 10000L)
    @Graded(points = 20, friendlyName = "Filtered View")
    public void test3_FilteredView() {
      startMainActivity(
          activity -> {
            // Check that the right number of courses are displayed initially
            onView(withId(R.id.recycler_view)).check(countRecyclerView(SUMMARY_COUNT));

            // Make sure blank searches work
            // Some manual delay is required for these tests to run reliably
            onView(withId(R.id.search)).perform(searchFor("  "));
            pause();
            onView(withId(R.id.recycler_view)).check(countRecyclerView(SUMMARY_COUNT));

            // Illinois has no super boring courses!
            onView(withId(R.id.search)).perform(searchFor("Super Boring Course"));
            pause();
            onView(withId(R.id.recycler_view)).check(countRecyclerView(0));

            // CS 124 should return one result
            onView(withId(R.id.search)).perform(searchFor("CS 124"));
            pause();
            onView(withId(R.id.recycler_view)).check(countRecyclerView(1));

            // study matches several courses
            onView(withId(R.id.search)).perform(searchFor("study"));
            pause();
            onView(withId(R.id.recycler_view)).check(countRecyclerView(9));
            onView(withRecyclerView(R.id.recycler_view).atPosition(2))
                .check(matches(hasDescendant(withText("IS 189: Independent Study"))));
          });
    }
  }
}

// md5: 2e7a6c6a88f6f64d1d298a18eb156337 // DO NOT REMOVE THIS LINE
