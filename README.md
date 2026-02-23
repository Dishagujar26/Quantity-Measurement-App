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
- Conversion logic is centralised and reusable
- Measurement value and unit are **encapsulated**
- `equals()` is overridden for **cross-unit value-based equality**
- Uses safe floating-point comparison
- Handles:

  - `null` values
  - invalid units
  - same reference checks
  - type mismatch safely
  
---

# Quantity Measurement App – UC4 (Extended Unit Support)

### 📌 Overview
 
- This module extends the generic Length class introduced in UC3 by adding support for Yards and Centimeters.
- It demonstrates how a well-designed generic solution scales to new units without code duplication.
- Supports equality comparison across `feet ↔ inches ↔ yards ↔ centimeters`.

### ⚙️ Use Case: UC4 – Extended Quantity Length Equality

- Accepts two numerical values along with their respective unit types
- Supports additional units: `YARDS` and `CENTIMETERS`
- Converts different units to a common base unit
- Compares values for equality
- Returns `true` if equivalent, otherwise `false`

### ⚙️ Key Implementation Points

- Continues using the single generic Length class
- Extends the existing LengthUnit enum with:
- YARDS `(1 yard = 3 feet)`
- CENTIMETERS `(1 cm = 0.393701 inches)`
- No changes required in Length class logic
- Conversion logic remains centralised in the enum
- Measurement value and unit stay encapsulated
- `equals()` supports cross-unit comparisons seamlessly
- Uses safe `floating-point comparison`

---

# Quantity Measurement App – UC5 (Unit-to-Unit Conversion)

### 📌 Overview

- This module extends UC4 by adding `explicit unit-to-unit conversion support` to the Quantity Measurement App.
- Instead of only `checking equality`, the `Length API` now allows `converting a measurement` from one unit to another using centralised conversion factors.
- Supports conversion across `feet ↔ inches ↔ yards ↔ centimeters`.

### ⚙️ Use Case: UC5 – Unit-to-Unit Conversion (Same Measurement Type)

- Accepts a numerical value along with a `source unit` and a `target unit`
- Supports conversion between all supported length units
- Normalises values using a `common base unit`
- Converts the normalised value into the target unit
- Returns the converted numeric value

### ⚙️ Key Implementation Points

- Continues using the same immutable `Length` class
- Reuses the existing `LengthUnit` enum with predefined conversion factors
- Conversion logic is centralised and consistent
- Supports both:
    - Static conversion using raw values
    - Instance-level conversion using `convertTo()`
- Validation ensures:
    - Units are `non-null` and valid
    - Values are finite (not NaN or infinite)
- Conversion preserves mathematical accuracy within `floating-point` tolerance
- No mutation of existing objects; conversions return new values or instances

---

# Quantity Measurement App – UC6 (Addition of Two Length Units)

### 📌 Overview

- This module enables addition operations between two length measurements.
- It supports adding lengths in the same or different units (within the length category) and returns the result in the unit of the first operand.
- For example, adding 1 foot and 12 inches yields 2 feet.

### ⚙️ Use Case: UC6 – Addition of Two Length Units (potentially different units)

- Accepts two numerical values with their respective units.
- Adds them and returns the sum in the unit of the first operand.

### ⚙️ Key Concepts Learned

- Addition of value objects with unit conversion.
- Immutability and safe handling of operands.
- Normalisation to a base unit for accurate arithmetic.
- Floating-point precision management.
- Commutativity and identity element behaviour.
- Robust validation for null or invalid inputs.

---

# Quantity Measurement App – UC7 (Addition with Target Unit Specification)

### 📌 Overview

- This module extends UC6 by allowing the caller to explicitly specify a target unit for addition results.
- Instead of defaulting to the first operand’s unit, the result can be returned in any supported unit.
-  Example: 1 foot + 12 inches with target unit YARDS ≈ 0.667 yards.

### ⚙️ Use Case: UC7 – Addition with Target Unit Specification

- Accepts two numerical values with their respective units and a target unit.
- Adds them and returns the sum in the explicitly specified target unit.

### ⚙️ Key Implementation Points (UC7 – Explicit Target Unit Addition)

- Uses the same immutable Length class and LengthUnit enum.
- Overloaded add() method:
   - UC6: add(A, B) → result in the first operand’s unit.
   - UC7: add(A, B, targetUnit) → result in explicitly specified unit.
- Private utility method handles conversion → addition → target unit conversion.
- Validation added: target unit must be non-null and valid.
- Preserves immutability, precision, and commutativity.
- Maintains backward compatibility with the UC6 addition.

---
