package accounts.web;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import rewards.internal.restaurant.RestaurantRepository;

@Component
public class RestaurantHealthCheck implements HealthIndicator {

  private RestaurantRepository restaurantRepository;

  public RestaurantHealthCheck(RestaurantRepository restaurantRepository) {
    this.restaurantRepository = restaurantRepository;
  }

  @Override
  public Health health() {

    Long count = restaurantRepository.getRestaurantCount();
    if (restaurantRepository != null && count.compareTo(0L) > 0) {
      return Health.up().build();
    }

    return Health
        .status("NO_RESTAURANTS")
        .withDetail("count", count)
        .build();
  }
}
