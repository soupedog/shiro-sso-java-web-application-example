import '../../public/style/default.css'

import React from 'react';
import {createRoot} from 'react-dom/client';

const container: Element | null = document.getElementById('root');

if (container != null) {
    const root = createRoot(container);

    root.render(
        <div>B</div>
    );
}