package at.fhv.teamb.symphoniacus.presentation;

import at.fhv.teamb.symphoniacus.application.DutyManager;
import at.fhv.teamb.symphoniacus.application.LoginManager;
import at.fhv.teamb.symphoniacus.application.MusicianManager;
import at.fhv.teamb.symphoniacus.persistence.model.Duty;
import at.fhv.teamb.symphoniacus.persistence.model.Musician;
import at.fhv.teamb.symphoniacus.persistence.model.Section;
import at.fhv.teamb.symphoniacus.persistence.model.User;
import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.model.Interval;
import com.calendarfx.view.CalendarView;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * This controller is responsible for handling all CalendarFX related actions such as
 * creating Calendar {@link Entry} objects, filling a {@link Calendar} and preparing it
 * for display.
 *
 * @author Dominic Luidold
 */
public class CalendarController implements Initializable {
    /**
     * Default interval start date represents {@link LocalDate#now()}.
     */
    private static final LocalDate DEFAULT_INTERVAL_START = LocalDate.now();

    /**
     * Default interval end date represents {@link #DEFAULT_INTERVAL_START} plus 13 days.
     */
    private static final LocalDate DEFAULT_INTERVAL_END = DEFAULT_INTERVAL_START.plusDays(13);

    /**
     * Extended interval end date represents {@link #DEFAULT_INTERVAL_START} plus one month.
     */
    private static final LocalDate EXTENDED_INTERVAL_END = DEFAULT_INTERVAL_START.plusMonths(1);

    @FXML
    private CalendarView calendarView;

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO - Temporarily used until proper login is introduced
        Optional<User> user = new LoginManager().login("vaubou", "eItFAJSb");
        Optional<Musician> musician = new MusicianManager().loadMusician(user.get());
        Section section = musician.get().getSection();

        this.calendarView.getCalendarSources().setAll(
            prepareCalendarSource(
                resources.getString("sections"),
                section
            )
        );
    }

    /**
     * Prepares a {@link CalendarSource} by creating a {@link Calendar} and subsequently filling
     * it with {@link Entry} objects.
     *
     * @param name    Name of the CalendarSource
     * @param section The section to use
     * @return A CalendarSource containing a Calendar and Entries
     */
    public CalendarSource prepareCalendarSource(String name, Section section) {
        CalendarSource calendarSource = new CalendarSource(name);
        Calendar calendar = createCalendar(section);
        fillCalendar(calendar, section);
        calendarSource.getCalendars().add(calendar);
        return calendarSource;
    }

    /**
     * Creates a {@link Calendar} based on specified {@link Section}.
     *
     * <p>The calendar will have a {@link Calendar.Style} assigned and will be marked as
     * {@code readOnly}.
     *
     * @param section The section to use
     * @return A Calendar
     */
    public Calendar createCalendar(Section section) {
        Calendar calendar = new Calendar(section.getDescription());
        calendar.setShortName(section.getSectionShortcut());
        calendar.setReadOnly(true);
        calendar.setStyle(Calendar.Style.STYLE1);
        return calendar;
    }

    /**
     * Fills a {@link Calendar} with {@link Entry} objects.
     *
     * @param calendar The calendar to fill
     * @param entries  A List of Entries
     */
    public void fillCalendar(Calendar calendar, List<Entry<Duty>> entries) {
        for (Entry<Duty> entry : entries) {
            entry.setCalendar(calendar);
        }
    }

    /**
     * Fills a {@link Calendar} with {@link Entry} objects based on specified {@link Section}.
     *
     * <p>Having no interval defined, the default interval start and end date (13 days from
     * {@link LocalDate#now()} are getting used.
     *
     * @param calendar The calendar to fill
     * @param section  The section used to determine required {@link Duty} objects
     * @see #DEFAULT_INTERVAL_START
     * @see #DEFAULT_INTERVAL_END
     */
    public void fillCalendar(Calendar calendar, Section section) {
        fillCalendar(calendar, createDutyCalendarEntries(
            new DutyManager().findAllInRangeWithSection(
                section,
                DEFAULT_INTERVAL_START,
                EXTENDED_INTERVAL_END // TODO - Temporarily used until lazy loading is introduced
            )
        ));
    }

    /**
     * Fills a {@link Calendar} with {@link Entry} objects based on supplied {@link Section} and
     * dates.
     *
     * @param calendar  The calendar to fill
     * @param section   The section used to determine which {@link Duty} objects
     * @param startDate Start date of the desired interval
     * @param endDate   End date of the desired interval
     */
    public void fillCalendar(
        Calendar calendar,
        Section section,
        LocalDate startDate,
        LocalDate endDate
    ) {
        fillCalendar(calendar, createDutyCalendarEntries(
            new DutyManager().findAllInRangeWithSection(
                section,
                startDate,
                endDate
            )
        ));
    }

    /**
     * Returns a list of CalendarFX {@link Entry} objects based on {@link Duty} objects.
     *
     * @param duties A List of Duties
     * @return A list of Entries
     */
    public List<Entry<Duty>> createDutyCalendarEntries(List<Duty> duties) {
        List<Entry<Duty>> calendarEntries = new LinkedList<>();
        for (Duty duty : duties) {
            Interval interval = new Interval(
                duty.getStart().toLocalDate(),
                duty.getStart().toLocalTime(),
                (duty.getEnd() == null // TODO - Adjust database to make duties always have an end
                    ? duty.getStart().toLocalDate() : duty.getEnd().toLocalDate()),
                (duty.getEnd() == null ? LocalTime.MAX : duty.getEnd().toLocalTime())
            );
            Entry<Duty> entry = new Entry<>(duty.getDescription(), interval);
            calendarEntries.add(entry);
        }
        return calendarEntries;
    }
}