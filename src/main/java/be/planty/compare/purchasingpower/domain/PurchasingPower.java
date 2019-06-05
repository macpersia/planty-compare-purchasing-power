package be.planty.compare.purchasingpower.domain;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * A PurchasingPower.
 */
@Document(collection = "purchasing_power")
public class PurchasingPower implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("year")
    private String year;

    @Field("city")
    private String city;

    @Field("category")
    private String category;

    @Field("value")
    private Double value;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getYear() {
        return year;
    }

    public PurchasingPower year(String year) {
        this.year = year;
        return this;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getCity() {
        return city;
    }

    public PurchasingPower city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCategory() {
        return category;
    }

    public PurchasingPower category(String category) {
        this.category = category;
        return this;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getValue() {
        return value;
    }

    public PurchasingPower value(Double value) {
        this.value = value;
        return this;
    }

    public void setValue(Double value) {
        this.value = value;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PurchasingPower)) {
            return false;
        }
        return id != null && id.equals(((PurchasingPower) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "PurchasingPower{" +
            "id=" + getId() +
            ", year='" + getYear() + "'" +
            ", city='" + getCity() + "'" +
            ", category='" + getCategory() + "'" +
            ", value=" + getValue() +
            "}";
    }
}
