terraform {
  backend "gcs" {
    bucket = "solwr-bucket"
  }
}

variable "SSH_PUB_KEY" {
  type = string
}

variable "GOOGLE_ZONE" {
  type = string
}

provider "google" {
  project = "solwr-413414"
  region  = "europe-west2"
}

# Define virtual machine for Spring Boot application
resource "google_compute_instance" "spring_boot_vm" {
  name         = "spring-boot-vm"
  machine_type = "e2-medium"
  zone         = var.GOOGLE_ZONE
  allow_stopping_for_update = true

  boot_disk {
    initialize_params {
      image = "ubuntu-os-cloud/ubuntu-2004-lts"
    }
  }

  # Install Java and setup Spring Boot application
  metadata_startup_script = <<EOF
#!/bin/bash
# Install Java 17
sudo apt-get update && sudo apt-get install -y openjdk-17-jdk
# Copy the Spring Boot application jar from Cloud Storage
sudo gsutil cp gs://solwr-bucket/myapp.jar /opt/
# Run your Spring Boot application (update the path and jar name accordingly)
sudo java -jar /opt/myapp.jar > /dev/null 2> /dev/null < /dev/null &
EOF


  # Applied firewall rules
  tags = ["spring-boot", "ssh"]

  network_interface {
    subnetwork = google_compute_subnetwork.default.id
    access_config {}
  }
  metadata = {
    ssh-keys = var.SSH_PUB_KEY
  }
}

resource "google_compute_network" "vpc_network" {
  auto_create_subnetworks = false
  name                    = "cloud-net"
}

resource "google_compute_subnetwork" "default" {
  name          = "cloud-subnet"
  ip_cidr_range = "10.0.1.0/24"
  region        = "europe-west2"
  network       = google_compute_network.vpc_network.id
}

# Firewall configuration to allow SSH
resource "google_compute_firewall" "ssh" {
  name           = "allow-ssh"
  network        = google_compute_network.vpc_network.id
  target_tags    = ["ssh"]
  allow {
    protocol = "tcp"
    ports    = ["22"]
  }
  source_ranges = ["0.0.0.0/0"]
}

# Firewall config to allow traffic to Spring Boot default port, test, test
resource "google_compute_firewall" "spring_boot" {
  name           = "spring-boot-app-firewall"
  network        = google_compute_network.vpc_network.id
  target_tags    = ["spring-boot"]
  allow {
    protocol = "tcp"
    ports    = ["8080"]
  }
  source_ranges = ["0.0.0.0/0"]
}