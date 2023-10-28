package edu.illinois.cs.cs124.ay2023.mp.models;

import androidx.annotation.NonNull;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Model holding the course summary information shown in the summary list.
 *
 * @noinspection unused
 */
public class Summary implements Comparable<Summary>{
  private String subject;

  /**
   * Get the subject for this Summary.
   *
   * @return the subject for this Summary
   */
  @NotNull
  public final String getSubject() {
    return subject;
  }

  private String number;

  /**
   * Get the number for this Summary.
   *
   * @return the number for this Summary
   */
  @NotNull
  public final String getNumber() {
    return number;
  }

  private String label;

  /**
   * Get the label for this Summary.
   *
   * @return the label for this Summary
   */
  @NotNull
  public final String getLabel() {
    return label;
  }

  /** Create an empty Summary. */
  public Summary() {}

  /**
   * Create a Summary with the provided fields.
   *
   * @param setSubject the department for this Summary
   * @param setNumber the number for this Summary
   * @param setLabel the label for this Summary
   */
  public Summary(@NonNull String setSubject, @NonNull String setNumber, @NotNull String setLabel) {
    subject = setSubject;
    number = setNumber;
    label = setLabel;
  }

  /** {@inheritDoc} */
  @NonNull
  @Override
  public String toString() {
    return subject + " " + number + ": " + label;
  }

  @Override
  public int compareTo(Summary o) {
    // Compare by number (as integers)
    int numberComparison = this.number.compareTo(o.number);
    if (numberComparison != 0) {
      return numberComparison;
    }

    // Compare by subject (entire string)
    int subjectComparison = this.subject.compareTo(o.subject);
    System.out.println("Subject comparison result: " + subjectComparison);
    if (subjectComparison != 0) {
      return subjectComparison;
    }
    return 0;
  }

  public static List<Summary> filter(List<Summary> list, String filter) {
    List<Summary> filteredList = new ArrayList<>();
    // Simple for loop to cycle through all summaries
    for (Summary summary : list) {
      // Checks: subject, number and label, if any of these contain the filter then add to the list
      if (summary.getSubject().contains(filter)
          || summary.getNumber().contains(filter)
          || summary.getLabel().contains(filter)) {
        filteredList.add(summary);
      }
    }
    // return the list
    return filteredList;
  }
}
