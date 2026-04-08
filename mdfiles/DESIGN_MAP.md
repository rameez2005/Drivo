# Assignment 2 Design Map - Drivo Project

## Layout Patterns (L1-L10) Mapping

| ID | File | Pattern | Description |
|---|---|---|---|
| L1 | `activity_main.xml` | CoordinatorLayout + Collapsing | Main dashboard with scrollable header |
| L2 | `item_driver_card.xml` | Constraint + Guideline + Barrier + Chain | Driver profile card |
| L3 | `layout_stats_bar.xml` | LinearLayout + weights | Three equal-width stat tiles |
| L4 | `layout_settings_row.xml` | RelativeLayout | Reusable settings row pattern |
| L5 | `layout_notification_bell.xml` | FrameLayout | Notification badge overlay |
| L6 | `layout_dashboard_grid.xml` | GridLayout + columnSpan | 6 action cards (2-column grid) |
| L7 | `layout_maintenance_table.xml` | TableLayout | Maintenance history data table |
| L8 | `layout_vehicle_carousel.xml` | HorizontalScrollView | Scrolling vehicle carousel |
| L9 | `layout_filter_bar.xml` | RadioGroup + Selector | Filter chips (styled buttons) |
| L10 | `layout_parts_tag_cloud.xml` | ConstraintLayout Flow | Wrapping tag cloud |

## Key Responsive Patterns

### 1. Weight Distribution (L3)
```xml
<LinearLayout android:weightSum="3">
  <LinearLayout android:layout_weight="1" android:layout_width="0dp" /> <!-- 1/3 -->
  <LinearLayout android:layout_weight="1" android:layout_width="0dp" /> <!-- 1/3 -->
  <LinearLayout android:layout_weight="1" android:layout_width="0dp" /> <!-- 1/3 -->
</LinearLayout>
```

### 2. Constraint Guideline (L2)
Vertical line at 22% keeps avatar narrow, text starts at guideline

### 3. Barrier Pattern (L2)
Imaginary line below the taller of {name, phone} - vehicle text positioned below to prevent overlap

### 4. Chain Distribution (L2)
3 chips linked in horizontal chain with `spread_inside` style - distributes evenly

### 5. Parallax Motion (L1)
Stats bar moves 50% speed of content scroll using `layout_collapseParallaxMultiplier="0.5"`

### 6. Pin Behavior (L1)
Toolbar pinned to top (always visible) while content scrolls using `layout_collapseMode="pin"`

## Color System

**Primary:** #1E5C9B (Professional Blue)
**Accent:** #FF8C00 (Orange)
**Status Active:** #4CAF50 (Green)
**Status Maintenance:** #FF9800 (Orange)
**Status Retired:** #F44336 (Red)
**Background:** #F5F7FA (Light Gray)
**Surface:** #FFFFFF (White)
**Text Primary:** #1A1A1A (Dark)
**Text Secondary:** #555555 (Gray)

## Drawable Assets (All XML, NO Images)

### Shapes
- `bg_card.xml` - White card with border
- `bg_chip.xml` - Light blue chip
- `bg_avatar_circle.xml` - Blue circle

### Vectors
- `ic_vehicle.xml` - Bus icon
- `ic_driver.xml` - Person icon
- `ic_salary.xml` - Money icon
- `ic_bell.xml` - Notification bell
- `ic_dispatch.xml` - Lightning bolt
- `ic_reports.xml` - Bar chart
- `ic_settings.xml` - Gear icon

### Selectors
- `bg_radio_chip.xml` - Checked/unchecked states

### LayerList
- `ic_avatar.xml` - Silhouette (head + body)

## Typography

- **H1** (24sp): Screen titles
- **H2** (20sp): Section headers
- **H3** (17sp): Card titles
- **Body1** (15sp): Normal text
- **Body2** (13sp): Secondary text
- **Caption** (12sp): Labels
- **Stat** (22sp): Large numbers

## Dummy Data

### Vehicles (5 total)
1. LEA-1234 - Toyota Coaster - ACTIVE
2. LEB-5678 - Hino Bus - ACTIVE
3. LHR-4321 - Toyota Hi-Ace - MAINTENANCE
4. ISB-9900 - Toyota Coaster - ACTIVE
5. KHI-0011 - Daewoo Bus - RETIRED

### Drivers (4 shown)
1. Ali Hassan - +92 300 1234567
2. Usman Tariq - +92 321 9876543
3. Bilal Ahmed - +92 333 5551234
4. Kamran Sheikh - +92 345 7890123

### Routes
- Lahore Industrial Estate → Shahdara
- SITE Karachi → Korangi
- Faisalabad Textile Mill → City Center

### Maintenance Parts (10)
Engine Oil, Brakes, Tyres, Battery, Clutch, Radiator, Gearbox, Air Filter, Timing Belt, Suspension

## Assignment 2 Rules Summary

✅ **DO:**
- Reference all strings in `strings.xml`
- Reference all colors in `colors.xml`
- Reference all dimensions in `dimens.xml`
- Use XML drawables only (no PNG/JPG)
- Keep MainActivity.kt minimal (only setContentView)

❌ **DON'T:**
- Hardcode text in layouts
- Hardcode colors (#RRGGBB)
- Hardcode dimensions (16dp)
- Use raster images
- Add Kotlin logic to MainActivity

