# TV Mode

VIEWit has a dedicated TV mode for Android TV and remote-controlled devices.

## Device Mode

- Mobile
- TV

Mobile is the default mode. TV mode is enabled manually in settings.

## TV Layout

The TV home screen uses horizontal rows and large 16:9 cards. Focused elements receive a bright white glow border. The layout is optimized for landscape displays.

## Remote Controls

Supported control concepts:

- D-Pad navigation
- Center / Enter activation
- Back handling
- Menu key browser overlay toggle
- Optional media key handling

## Browser Overlay

The TV browser includes a control overlay with:

- Home
- Back
- Forward
- Reload
- External
- Close

The overlay can be shown by default and toggled with the Menu key.

## Settings

TV settings include:

- Device Mode
- TV Layout Scale
- Show TV Control Overlay
- Auto Hide Browser Controls
- Focus Glow Intensity
- Start in Last Web App
- Remote Back Behavior
- D-Pad Scroll Speed
- Enable Media Keys
- Show Large Cards
- Prefer Landscape Layout

## V1.1.2 TV Browser Viewport

TV browser mode suppresses horizontal D-Pad scrolling and injects a no-horizontal-overflow rule after page load. This is intended to reduce unwanted side scrolling on wide desktop websites such as SoundCloud and Spotify while keeping vertical D-Pad scrolling available.
