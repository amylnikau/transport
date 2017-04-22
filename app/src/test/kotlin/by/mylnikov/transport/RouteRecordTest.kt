package by.mylnikov.transport

import by.mylnikov.transport.model.RouteRecord
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import java.util.*


class RouteRecordTest {
    @Test
    fun testRecordsSorting() {
        val test_items = Arrays.asList(
                RouteRecord("", "", "14:15", "15:20", "", ""),
                RouteRecord("", "", "11:44", "12:11", "", ""),
                RouteRecord("", "", "11:23", "13:45", "", ""),
                RouteRecord("", "", "21:10", "23:25", "", ""))
        val expected_items = Arrays.asList(
                RouteRecord("", "", "11:23", "13:45", "", ""),
                RouteRecord("", "", "11:44", "12:11", "", ""),
                RouteRecord("", "", "14:15", "15:20", "", ""),
                RouteRecord("", "", "21:10", "23:25", "", ""))

        Collections.sort(test_items)

        assertThat(test_items, `is`(expected_items))
    }

    @Test
    fun testRecordsSortingWithSameDepartureTime() {
        val test_items = Arrays.asList(
                RouteRecord("", "", "14:15", "15:20", "", ""),
                RouteRecord("", "", "14:15", "14:25", "", ""),
                RouteRecord("", "", "14:15", "15:41", "", ""))
        val expected_items = Arrays.asList(
                RouteRecord("", "", "14:15", "14:25", "", ""),
                RouteRecord("", "", "14:15", "15:20", "", ""),
                RouteRecord("", "", "14:15", "15:41", "", ""))

        Collections.sort(test_items)

        assertThat(test_items, `is`(expected_items))
    }
}