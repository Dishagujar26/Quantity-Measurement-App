# Quantity Measurement App – UC1 (Feet Equality)

### 📌 Overview

- This module checks whether two measurements given in feet are equal.
- It focuses on correct `object equality`, `safe floating-point comparison`, and clean OOP design.

### ⚙️ Use Case: UC1 – Feet Measurement Equality

- Accepts two numerical values in feet
- Compares them for equality
- Returns `true` if equal, otherwise false

### ⚙️ Key Implementation Points

- Uses a separate Feet class to represent a measurement
- Measurement value is `private` and `final` (immutable)
- `equals()` is overridden correctly
- `Double.compare()` is used instead of `==`
- Handles `null`, type mismatch, and same reference cases safely

---

# Quantity Measurement App – UC2 (Inches Equality)

### 📌 Overview

- This module checks whether two measurements given in **inches** are equal.
- It extends UC1 by supporting equality checks for inches while following the same design principles.

### ⚙️ Use Case: UC2 – Inches Measurement Equality

- Accepts two numerical values in inches
- Compares them for equality
- Returns `true` if equal, otherwise false

### ⚙️ Key Implementation Points

- Uses a separate **Inches** class to represent a measurement
- Measurement value is `private` and `final` (immutable)
- `equals()` is overridden correctly
- `Double.compare()` is used instead of `==`
- Handles `null`, type mismatch, and same reference cases safely

---

# Quantity Measurement App – UC3 (Generic Quantity Length)

### 📌 Overview

- This module refactors Feet and Inches into a **single generic Length class**.
- It eliminates code duplication by applying the **DRY (Don’t Repeat Yourself) principle**.
- Supports equality comparison **across different units** (feet ↔ inches).


### ⚙️ Use Case: UC3 – Generic Quantity Length Equality

- Accepts two numerical values along with their respective unit types
- Converts different units to a **common base unit**
- Compares values for equality
- Returns `true` if equivalent, otherwise false


### ⚙️ Key Implementation Points

- Uses a **single Length class** to represent all length measurements
- Introduces a `LengthUnit` **enum** for supported units and conversion factors
- Eliminates separate Feet and Inches classes
- Conversion logic is centralized and reusable
- Measurement value and unit are **encapsulated**
- `equals()` is overridden for **cross-unit value-based equality**
- Uses safe floating-point comparison
- Handles:

  - `null` values
  - invalid units
  - same reference checks
  - type mismatch safely
  
---




