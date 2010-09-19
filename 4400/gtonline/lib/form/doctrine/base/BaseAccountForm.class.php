<?php

/**
 * Account form base class.
 *
 * @method Account getObject() Returns the current form's model object
 *
 * @package    gtonline
 * @subpackage form
 * @author     Your name here
 * @version    SVN: $Id: sfDoctrineFormGeneratedTemplate.php 29553 2010-05-20 14:33:00Z Kris.Wallsmith $
 */
abstract class BaseAccountForm extends BaseFormDoctrine
{
  public function setup()
  {
    $this->setWidgets(array(
      'id'        => new sfWidgetFormInputHidden(),
      'email'     => new sfWidgetFormInputText(),
      'password'  => new sfWidgetFormInputText(),
      'firstname' => new sfWidgetFormInputText(),
      'lastname'  => new sfWidgetFormInputText(),
      'isAdmin'   => new sfWidgetFormInputCheckbox(),
    ));

    $this->setValidators(array(
      'id'        => new sfValidatorChoice(array('choices' => array($this->getObject()->get('id')), 'empty_value' => $this->getObject()->get('id'), 'required' => false)),
      'email'     => new sfValidatorString(array('max_length' => 255)),
      'password'  => new sfValidatorString(array('max_length' => 255)),
      'firstname' => new sfValidatorString(array('max_length' => 255)),
      'lastname'  => new sfValidatorString(array('max_length' => 255)),
      'isAdmin'   => new sfValidatorBoolean(),
    ));

    $this->validatorSchema->setPostValidator(
      new sfValidatorDoctrineUnique(array('model' => 'Account', 'column' => array('email')))
    );

    $this->widgetSchema->setNameFormat('account[%s]');

    $this->errorSchema = new sfValidatorErrorSchema($this->validatorSchema);

    $this->setupInheritance();

    parent::setup();
  }

  public function getModelName()
  {
    return 'Account';
  }

}
