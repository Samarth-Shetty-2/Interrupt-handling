# 🖥️ Interrupt Controller Simulation (Java)

## 🎯 Objective
This project simulates the working of an **Interrupt Controller** in a computer system, demonstrating how **priorities** and **masking** affect device interrupt handling.

## 🧩 Features
- Simulates three I/O devices:
  - **Keyboard** – High Priority  
  - **Mouse** – Medium Priority  
  - **Printer** – Low Priority  
- Supports **interrupt masking/unmasking** at runtime.
- Always serves the **highest-priority interrupt available**.
- Simulates **asynchronous interrupt generation** using multithreading.
- Logs each **ISR execution timestamp** in memory.
- Displays clear runtime messages.

## 🛠️ Technologies Used
- Language: **Java**
- Concepts: Multithreading, Priority Queue, Synchronization, Event Simulation
