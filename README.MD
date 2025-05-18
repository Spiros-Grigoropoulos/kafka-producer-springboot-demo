This is a sample application for a Kafka Producer in Springboot

The application creates a REST endpoint "/v1/cartEvent" where it expects to get a JSON body through a POST call with the following schema:

{
  "$schema": "https://json-schema.org/draft/2020-12/schema",
  "type": "object",
  "properties": {
    "cartEventType": {
      "type": "string",
      "enum": ["PAID", "ADD", "REMOVED", "CHECKOUT_START", "CHECKOUT_CANCEL"]
    },
    "cartId": {
      "type": "integer"
    },
    "product": {
      "type": "object",
      "properties": {
        "productId": {
          "type": "integer"
        },
        "productName": {
          "type": "string"
        },
        "price": {
          "type": "string",
          "pattern": "[€$]\d+(?:\.\d{1,2})?"
        },
        "quantity": {
          "type": "integer",
          "minimum": 1
        }
      },
      "required": ["productId", "productName", "price", "quantity"]
    },
    "sessionId": {
      "type": "string",
      "format": "uuid"
    },
    "userID": {
      "type": "integer"
    }
  },
  "required": ["cartEventType", "cartId", "product", "sessionId", "userID"]
}

As an example a JSON like is accepted:

{
    "cartEventType": "ADD",
    "cartId": 17869084,
    "product": {
        "productId":56745310,
        "productName":"ASUS ROG RTX 5090",
        "price":"$4000",
        "quantity":2
    },
    "sessionId": "daf55c09-b9dd-45c1-9b11-f36ac7781f4c",
    "userID": 86908432
}

The endpoint is handled by 'CartEventController' which uses the SpringBoot annotation '@RestController' to be build by Springboot automatically.

The application creates a topic 'cart-events' with 3 partitions if it does not exist.
This is done in' AutoCreateTopicConfig' when the app starts

Once the messages are processed from service they are published to Kafka using the cartId as the key value.
This is handled in the 'CartEventProducer' class that creates the message and publish it asynchronously via completableFuture() methods.
To publish data the "KafkaTemplate" class is used that is provided by the 'org.apache.kafka.*' library.

