from confluent_kafka import Producer


kafka_config = {
    'bootstrap.servers': 'localhost:9094'
}

producer = Producer(kafka_config)

