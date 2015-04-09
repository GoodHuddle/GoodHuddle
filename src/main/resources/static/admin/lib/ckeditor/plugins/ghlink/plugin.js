/*
 * (C) Copyright ${year} Nuxeo SA (http://nuxeo.com/) and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */

CKEDITOR.plugins.add( 'ghlink',
{
  init: function( editor )
  {
    editor.addCommand( 'insertLink',
      {
        exec : function( editor )
        {
          var selection = window.getSelection();
          editor.addGhLink(editor, selection);
        }
      });
    editor.ui.addButton( 'Ghlink',
    {
      label: 'Insert Link',
      command: 'insertLink',
      icon: this.path + 'images/ghlink.png'
    });

    // Override dbl click on links to open plugin
    editor.on('doubleclick', function(evt){
      var element = evt.data.element;

      if ( element.is( 'a' )) {
        evt.stop();
        var selection = window.getSelection();
        editor.addGhLink(editor, selection, evt);
      }
    }, 1000);
  }
} );
